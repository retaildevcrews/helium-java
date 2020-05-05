package com.cse.helium.app.services.keyvault;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.certificates.CertificateAsyncClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;
import com.azure.security.keyvault.keys.KeyAsyncClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.SecretAsyncClient;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;
import com.cse.helium.app.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KeyVaultService implements IKeyVaultService {
  private final String keyVaultName;
  private String authType = "";
  private KeyAsyncClient keyAsyncClient;
  private SecretClient secretClient;
  private SecretAsyncClient secretAsyncClient;
  private CertificateAsyncClient certificateAsyncClient;
  private DefaultAzureCredential credential; 

  private static final Logger logger =   LogManager.getLogger(KeyVaultService.class);

  /**
   * KeyVaultService.
   */
  public KeyVaultService(IEnvironmentReader environmentReader)
      throws Exception {

    this.keyVaultName = environmentReader.getKeyVaultName();
    logger.info("KeyVaultName is " + this.keyVaultName);
    this.authType = environmentReader.getAuthType();
    logger.info("Auth type is " + this.authType);

    HttpLogDetailLevel logDetailLevel = getKvLogDetailLevel();

    //build credential based on authType flag
    if (this.authType.equals(Constants.USE_MSI)) {
      credential = new DefaultAzureCredentialBuilder()
          .excludeEnvironmentCredential()
          .excludeAzureCliCredential()
          .excludeSharedTokenCacheCredential()
          .build();
    } else if (this.authType.equals(Constants.USE_CLI)) {
      credential = new DefaultAzureCredentialBuilder()
        .excludeEnvironmentCredential()
        .excludeManagedIdentityCredential()
        .excludeSharedTokenCacheCredential()
        .build();
    } else if (this.authType.equals(Constants.USE_MSI_APPSVC)) {
      try {
        credential = new DefaultAzureCredentialBuilder()
        .excludeEnvironmentCredential()
        .excludeAzureCliCredential()
        .excludeSharedTokenCacheCredential()
        .build();
      } catch (final Exception ex) {
        logger.error(ex.getMessage());
        throw ex;
      }
    } else {
      this.authType = Constants.USE_MSI;
      credential = new DefaultAzureCredentialBuilder()
      .excludeEnvironmentCredential()
      .excludeAzureCliCredential()
      .excludeSharedTokenCacheCredential()
      .build();
    }

    //build KeyVault clients
    secretClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .buildClient();

    secretAsyncClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .httpLogOptions(new HttpLogOptions().setLogLevel(logDetailLevel))
        .buildAsyncClient();

    //build key client
    keyAsyncClient = new KeyClientBuilder()
      .vaultUrl(getKeyVaultUri())
      .credential(credential)
      .buildAsyncClient();
  
    //build certificate client
    certificateAsyncClient = new CertificateClientBuilder()
    .vaultUrl(getKeyVaultUri())
    .credential(credential)
    .buildAsyncClient();
  }

  private HttpLogDetailLevel getKvLogDetailLevel() {
    
    Level currentLogLevel = logger.getLevel();
    HttpLogDetailLevel returnLevel = HttpLogDetailLevel.NONE;

    if (currentLogLevel == Level.TRACE 
        || currentLogLevel == Level.DEBUG 
        || currentLogLevel == Level.INFO) {

      returnLevel = HttpLogDetailLevel.HEADERS;
    } else if (currentLogLevel == Level.WARN 
          || currentLogLevel == Level.ERROR
          || currentLogLevel == Level.FATAL) {

      returnLevel = HttpLogDetailLevel.BASIC;
    }

    return returnLevel;
  }

  /**
   * getKeyVaultUri.
   */
  public String getKeyVaultUri() {
    String kvUri = "";

    if (keyVaultName != null && !keyVaultName.isEmpty()) {
      if (keyVaultName.toUpperCase().startsWith("HTTPS://")) {
        kvUri = keyVaultName;
      } else {
        kvUri = "https://" + keyVaultName + ".vault.azure.net";
      }
    }
    logger.info("KeyVaultUrl is " + kvUri);
    return kvUri;
  }

  /**
   * getSecret.
   */
  public Mono<String> getSecretValue(final String secretName) {
    logger.info("Secrets in getSecret are " + (secretName == null ? "NULL" : "NOT NULL"));
    return  getSecret(secretName)
      .map(keyVaultSecret -> {
        return keyVaultSecret.getValue();
      }); 
  }

  public Mono<KeyVaultSecret> getSecret(final String secretName) {
    return  secretAsyncClient.getSecret(secretName);
  }

  /**
   * listSecretsSync.
   * Returns null on error and logs error.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public List<SecretProperties> listSecretsSync() {
    List<SecretProperties> listSecretProps = new ArrayList<SecretProperties>();
    
    Iterator<SecretProperties> secretPropsIterator = secretClient
        .listPropertiesOfSecrets()
        .iterator();
        
    secretPropsIterator.forEachRemaining(secretProps -> {
      listSecretProps.add(secretProps);
    });

    return listSecretProps;
  }

  /**
   * getSecretsSync.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public Map<String, String> getSecretsSync() {
    final List<SecretProperties> secretItems = listSecretsSync();

    final Map<String, String> secrets = new ConcurrentHashMap<String, String>();
    try {
      secretItems.forEach(item -> {
        final String itemName = item.getName();
        final String secretValue = getSecretValue(itemName).block();
        logger.info("lengths of secretItem name and value are " + itemName.length()
            + " " + secretValue.length());
        secrets.put(itemName, secretValue);
      });
    } catch (Exception ex) {
      logger.error("Exception retrieving secrets from keyvault ", ex);
      throw ex;
    }
    return secrets;
  }

  /**
   * getKey.
   */
  public Mono<KeyVaultKey> getKey(final String keyName) {
    return  keyAsyncClient.getKey(keyName);
  }

  /**
   * getCertificate.
   */
  public Mono<KeyVaultCertificateWithPolicy> getCertificate(final String certName) {
    return certificateAsyncClient.getCertificate(certName);
  }
}

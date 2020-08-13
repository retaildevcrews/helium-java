package com.cse.helium.app.services.keyvault;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
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
import com.cse.helium.app.error.HeliumException;
import java.text.MessageFormat;
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
@SuppressWarnings("squid:S1612") // Suppressing requirement to move lambda to function
public class KeyVaultService implements IKeyVaultService {
  private final String keyVaultName;
  private String authType = "";
  private KeyAsyncClient keyAsyncClient;
  private SecretClient secretClient;
  private SecretAsyncClient secretAsyncClient;
  private CertificateAsyncClient certificateAsyncClient;
  private TokenCredential credential; 

  private static final Logger logger =   LogManager.getLogger(KeyVaultService.class);

  /**
   * KeyVaultService.
   */
  public KeyVaultService(IEnvironmentReader environmentReader)
      throws HeliumException {

    this.keyVaultName = environmentReader.getKeyVaultName();
    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("KeyVaultName is {0}", this.keyVaultName));
    }

    this.authType = environmentReader.getAuthType();

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("Auth type is {0}", this.authType));
    }

    //build credential based on authType flag
    if (this.authType.equals(Constants.USE_MI)) {

      credential = new ManagedIdentityCredentialBuilder().build();

    } else if (this.authType.equals(Constants.USE_CLI)) {
      
      credential = new AzureCliCredentialBuilder().build();
      
    } else if (this.authType.equals(Constants.USE_MI_APPSVC)) {
      try {
        credential = new ManagedIdentityCredentialBuilder().build();
      } catch (final Exception ex) {
        logger.error(ex.getMessage());
        throw new HeliumException(ex.getMessage());
      }
    } else {
      this.authType = Constants.USE_MI;
      credential = new ManagedIdentityCredentialBuilder().build();
    }


    //build KeyVault clients
    secretClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .addPolicy(getKvLogPolicy())
        .buildClient();

    secretAsyncClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .addPolicy(getKvLogPolicy())
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

  private KeyVaultSecretsLogPolicy getKvLogPolicy() {
    
    Level currentLogLevel = logger.getLevel();
    HttpLogDetailLevel logLevel = HttpLogDetailLevel.NONE;

    if (currentLogLevel == Level.TRACE 
        || currentLogLevel == Level.DEBUG) {

      logLevel = HttpLogDetailLevel.BODY_AND_HEADERS;


    } else if (currentLogLevel == Level.INFO) {

      logLevel = HttpLogDetailLevel.HEADERS;

    } else if (currentLogLevel == Level.WARN 
          || currentLogLevel == Level.ERROR
          || currentLogLevel == Level.FATAL) {

      logLevel = HttpLogDetailLevel.BASIC;
    }

    HttpLogOptions options = new HttpLogOptions()
        .setLogLevel(logLevel)
        .setPrettyPrintBody(true);

    return new KeyVaultSecretsLogPolicy(options);
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
    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("KeyVaultUrl is {0}", kvUri));
    }
    return kvUri;
  }

  /**
   * getSecret.
   */
  public Mono<String> getSecretValue(final String secretName) {
    
    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("Secrets in getSecret are {0}", 
          secretName == null ? "NULL" : "NOT NULL"));
    }

    return  getSecret(secretName)
      .map(keyVaultSecret -> keyVaultSecret.getValue());
  }

  public Mono<KeyVaultSecret> getSecret(final String secretName) {
    return  secretAsyncClient.getSecret(secretName);
  }

  /**
   * listSecretsSync.
   * Returns null on error and logs error.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public List<SecretProperties> listSecrets() {
    List<SecretProperties> listSecretProps = new ArrayList<>();
    
    Iterator<SecretProperties> secretPropsIterator = secretClient
        .listPropertiesOfSecrets()
        .iterator();
        
    secretPropsIterator.forEachRemaining(secretProps -> 
        listSecretProps.add(secretProps));

    return listSecretProps;
  }

  /**
   * getSecretsSync.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public Map<String, String> getSecrets() {
    final List<SecretProperties> secretItems = listSecrets();

    final Map<String, String> secrets = new ConcurrentHashMap<>();
    try {
      secretItems.forEach(item -> {
        final String itemName = item.getName();
        final String secretValue = getSecretValue(itemName).block();
        if (logger.isInfoEnabled()) {
          logger.info(MessageFormat.format("lengths of secretItem name and value are {0} {1}",
              itemName.length(),
              secretValue.length()));
        }
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

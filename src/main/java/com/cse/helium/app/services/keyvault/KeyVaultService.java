package com.cse.helium.app.services.keyvault;

import com.cse.helium.app.Constants;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.credentials.AzureCliCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.models.SecretItem;
import com.microsoft.rest.ServiceCallback;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KeyVaultService implements IKeyVaultService {
  private final String keyVaultName;
  private String authType = "";
  private AzureTokenCredentials azureTokenCredentials;
  private final KeyVaultClient keyVaultClient;


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

    if (this.authType.equals(Constants.USE_MSI)) {
      azureTokenCredentials = new MSICredentials(AzureEnvironment.AZURE);
    } else if (this.authType.equals(Constants.USE_CLI)) {
      try {
        azureTokenCredentials = AzureCliCredentials.create();
      } catch (final IOException ex) {
        logger.error(ex.getMessage());
        throw ex;
      }
    } else if (this.authType.equals(Constants.USE_MSI_APPSVC)) {
      try {
        azureTokenCredentials = new AppServiceMSICredentials(AzureEnvironment.AZURE);
      } catch (final Exception ex) {
        logger.error(ex.getMessage());
        throw ex;
      }
    } else {
      this.authType = Constants.USE_MSI;
      azureTokenCredentials = new MSICredentials(AzureEnvironment.AZURE);
    }

    keyVaultClient = new KeyVaultClient(azureTokenCredentials);
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
  public Mono<String> getSecret(final String secretName) {
    logger.info("Secrets in getSecret are " + (secretName == null ? "NULL" : "NOT NULL"));
    return Mono.create(sink -> {
      keyVaultClient.getSecretAsync(getKeyVaultUri(), secretName,
          new ServiceCallback<SecretBundle>() {

            @Override
            public void success(final SecretBundle secret) {
              sink.success(secret.value());
            }

            @Override
            public void failure(final Throwable error) {
              sink.error(error);
            }
          });
    });
  }

  /**
   * listSecretsSync.
   * Returns null on error and logs error.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public List<SecretItem> listSecretsSync() {
    List<SecretItem> secrets = null;
    try {
      secrets = keyVaultClient.listSecretsAsync(getKeyVaultUri(), null).get();
      logger.info("Secrets in listSecretsSync are " + (secrets == null ? "NULL" : "NOT NULL"));
    } catch (final Exception exception) {
      logger.error(exception.getMessage());
    }

    return secrets;
  }

  /**
   * listSecretsSync.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public Map<String, String> getSecretsSync() {
    final List<SecretItem> secretItems = listSecretsSync();

    logger.info("secretItems variable is" + secretItems);
    final Map<String, String> secrets = new ConcurrentHashMap<String, String>();
    try {
      secretItems.forEach(item -> {
        logger.info("secretItem single in the loop is " + (item == null ? "NULL" : "NOT NULL"));
        final String itemName = item.id().substring(item.id().lastIndexOf("/") + 1);
        final String secretValue = getSecret(itemName).block();
        logger.info("lengths of secretItem name and value are " + itemName.length()
            + " " + secretValue.length());
        secrets.put(itemName, secretValue);
      });
    } catch (Exception ex) {
      logger.error("Hit Exception getting the secrets from keyvault ", ex);
      throw ex;
    }
    return secrets;
  }

  /**
   * getKey.
   */
  public Mono<KeyBundle> getKey(final String keyName) {
    return Mono.create(sink -> {
      keyVaultClient.getKeyAsync(getKeyVaultUri(), keyName, new ServiceCallback<KeyBundle>() {
        @Override
        public void success(final KeyBundle keyBundle) {
          sink.success(keyBundle);
        }

        @Override
        public void failure(final Throwable error) {
          sink.error(error);
        }
      });
    });
  }

  /**
   * getCertificate.
   */
  public Mono<CertificateBundle> getCertificate(final String certName) {
    return Mono.create(sink -> {
      keyVaultClient.getCertificateAsync(getKeyVaultUri(), certName,
          new ServiceCallback<CertificateBundle>() {
            @Override
            public void success(final CertificateBundle certBundle) {
              sink.success(certBundle);
            }

            @Override
            public void failure(final Throwable error) {
              sink.error(error);
            }
          });
    });
  }
}

package com.microsoft.cse.helium.app.services.keyvault;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AzureCliCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.models.SecretItem;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.rest.ServiceCallback;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KeyVaultService implements IKeyVaultService {
  private final String keyVaultName;
  private String environmentFlag = "";
  private AzureTokenCredentials azureTokenCredentials;
  private final KeyVaultClient keyVaultClient;

  private final String keyVaultNameRegex = "^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";
  private static final Logger logger = LoggerFactory.getLogger(KeyVaultService.class);



  /**
   * KeyVaultService.
   */
  public KeyVaultService(IEnvironmentReader environmentReader)
      throws Exception {

    this.keyVaultName = environmentReader.getKeyVaultName();
    this.environmentFlag = environmentReader.getAuthType();

    if (this.environmentFlag.equals(Constants.USE_MSI)) {
      azureTokenCredentials = new MSICredentials(AzureEnvironment.AZURE);
    } else if (this.environmentFlag.equals(Constants.USE_CLI)) {
      try {
        azureTokenCredentials = AzureCliCredentials.create();
      } catch (final IOException ex) {
        logger.error(ex.getMessage());
        throw ex;
      }
    } else {
      this.environmentFlag = Constants.USE_MSI;
    }

    if (!checkKeyVaultName(this.keyVaultName)) {
      logger.error("helium.keyvault.name (KeyVaultName) value is '" + this.keyVaultName
          + "' which does not meet the criteria must be 3-24 characters long, begin with a "
          + "character, may contain alphanumeric or hyphen, no repeating hyphens, and end with "
          + "alphanumeric.  Check ${KeyVaultName} in your environment variables.");
      throw new IllegalArgumentException("helium.keyvault.name (value='" + this.keyVaultName
          + "') must be 3-24 characters long, begin with a character, may contain alphanumeric or"
          + " hyphen, no repeating hyphens, and end with alphanumeric.  Check ${KeyVaultName} in"
          + " your environment variables.");
    }

    keyVaultClient = new KeyVaultClient(azureTokenCredentials);

  }


  /**
   * checkKeyVaultName.
   */
  private Boolean checkKeyVaultName(final String keyVaultName) {
    Boolean validSetting = true;

    if (keyVaultName.length() < 3 || keyVaultName.length() > 24) {
      validSetting = false;
    }

    // validate key vault name with regex:
    // ^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$
    // ^[a-zA-Z] - start of string, must be a alpha character
    // (?!.*--) - look ahead and make sure there are no double hyphens (e.g., "--")
    // [a-zA-Z0-9-]* - match alphanumeric and hyphen as many times as needed
    // [a-zA-Z0-9] - final character must be alphanumeric
    if (!keyVaultName.matches(keyVaultNameRegex)) {
      validSetting = false;
    }

    return validSetting;
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
    return kvUri;
  }

  /**
   * getSecret.
   */
  public Mono<String> getSecret(final String secretName) {
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

    final Map<String, String> secrets = new ConcurrentHashMap<String, String>();
    secretItems.forEach(item -> {
      final String itemName = item.id().substring(item.id().lastIndexOf("/") + 1);
      final String secretValue = getSecret(itemName).block();
      secrets.put(itemName, secretValue);
    });
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

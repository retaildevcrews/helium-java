package com.microsoft.cse.helium.app.services.keyvault;

import com.microsoft.cse.helium.app.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentReader implements IEnvironmentReader {

    public String getAuthType() {
        String authType = System.getenv(Constants.AUTH_TYPE);

        // If it is not set, use the MSI
        if(authType == null) {
            return Constants.USE_MSI;
        }

        // ONLY If it is set and values are either MSI or CLI, we will accept.
        // otherwise, default is just the MSI
        if (authType.equals(Constants.USE_MSI)) {
            return Constants.USE_MSI;
          } else if (authType.equals(Constants.USE_CLI)) {
              return Constants.USE_CLI;
          } else {
            return Constants.USE_MSI;
          }
    }

    public String getKeyVaultName() {
        String keyVaultName =  System.getenv(Constants.KEY_VAULT_NAME);
        if(keyVaultName == null || StringUtils.isEmpty(keyVaultName)) {
            throw new IllegalArgumentException("keyVaultName");
        }
        return keyVaultName;
    }

}
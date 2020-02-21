package com.microsoft.cse.helium.app.services.keyvault;


interface IKeyVaultService {

    String getKey(String keyName);
    String getSecret (String secretName);
    String getCertificate (String certName);
}

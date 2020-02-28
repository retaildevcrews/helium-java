package com.microsoft.cse.helium.app.services.keyvault;

import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;

public interface IKeyVaultService {

    String getSecret (String secretName);
    KeyBundle getKey(String keyName);
    CertificateBundle getCertificate (String certName);
}

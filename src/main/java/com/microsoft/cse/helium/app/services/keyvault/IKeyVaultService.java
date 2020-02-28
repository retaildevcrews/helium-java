package com.microsoft.cse.helium.app.services.keyvault;

import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;

import reactor.core.publisher.Mono;

public interface IKeyVaultService {

    Mono<String> getSecret (String secretName);
    KeyBundle getKey(String keyName);
    CertificateBundle getCertificate (String certName);
}

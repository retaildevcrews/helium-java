package com.microsoft.cse.helium.app.services.keyvault;
import com.microsoft.azure.keyvault.models.SecretItem;

import java.util.*;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;

import reactor.core.publisher.Mono;

public interface IKeyVaultService {

    Mono<String> getSecret (String secretName);
    Mono<List<SecretItem>> listSecrets();
    Mono<KeyBundle> getKey(String keyName);
    Mono<CertificateBundle> getCertificate (String certName);
    Map<String, String> getSecrets();
}
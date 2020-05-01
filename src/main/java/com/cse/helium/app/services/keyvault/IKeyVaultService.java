package com.cse.helium.app.services.keyvault;

import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface IKeyVaultService {

  Mono<KeyVaultSecret> getSecret(String secretName);
  
  Mono<String> getSecretValue(String secretName);

  Mono<KeyVaultKey> getKey(String keyName);
  
  Mono<KeyVaultCertificateWithPolicy> getCertificate(String certName);
  
  List<SecretProperties> listSecrets();

  Map<String, String> getSecrets();
}
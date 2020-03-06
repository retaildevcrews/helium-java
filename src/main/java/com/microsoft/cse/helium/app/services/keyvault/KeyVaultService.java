package com.microsoft.cse.helium.app.services.keyvault;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.AzureCliCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.models.SecretItem;
import com.microsoft.rest.ServiceCallback;

import reactor.core.publisher.Mono;

@Service
public class KeyVaultService implements IKeyVaultService
{
    private String _keyVaultName;
    private String _environmentFlag = "";
    private AzureTokenCredentials _credentials;
    private KeyVaultClient _keyVaultClient;

    private final String _keyVaultNameRegex ="^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";
    private static final Logger _logger = LoggerFactory.getLogger(KeyVaultService.class);

    public static final String USE_MSI="MSI";
    public static final String USE_CLI="CLI";

    public KeyVaultService (@Value("${helium.keyvault.name}") String keyVaultName, @Value("${helium.environment.flag}") String environmentFlag) throws Exception {
        _keyVaultName = keyVaultName.trim().toUpperCase();
        _environmentFlag = environmentFlag.trim().toUpperCase();

        if(_environmentFlag.equals(USE_MSI)) {
            _credentials = new MSICredentials(AzureEnvironment.AZURE);
        } else if (_environmentFlag.equals(USE_CLI)) {
            try{
                _credentials = AzureCliCredentials.create();
            }
            catch (IOException ex){
                _logger.error(ex.getMessage());
                throw(ex);
            }
        } else {
            _logger.error("helium.environment.flag (He_EnvironmentFlag) value is '" + _environmentFlag + "' it must be set to 'CLI' or 'MSI'");
            throw new IllegalArgumentException("helium.environment.flag (value='" + _environmentFlag + "') must be 'MSI' or 'CLI'.  Check ${He_EnvironmentFlag} in your environment variables.");
        }
        
        if(!checkKeyVaultName(_keyVaultName)) {
            _logger.error("helium.keyvault.name (KeyVaultName) value is '" + _keyVaultName + "' which does not meet the criteria must be 3-24 characters long, begin with a character, may contain alphanumeric or hyphen, no repeating hyphens, and end with alphanumeric.  Check ${KeyVaultName} in your environment variables.");
            throw new IllegalArgumentException("helium.keyvault.name (value='" + _keyVaultName + "') must be 3-24 characters long, begin with a character, may contain alphanumeric or hyphen, no repeating hyphens, and end with alphanumeric.  Check ${KeyVaultName} in your environment variables.");
        }

        _keyVaultClient = new KeyVaultClient(_credentials);

    }

    public static KeyVaultService create(String keyVaultName, String environmentFlag) throws Exception{
        return new KeyVaultService (keyVaultName, environmentFlag);
    }

    private Boolean checkKeyVaultName(String keyVaultName){
        Boolean validSetting = true;

        if(keyVaultName.length() < 3 || keyVaultName.length() > 24){
            validSetting= false;
        }

        // validate key vault name with regex: ^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$
        //    ^[a-zA-Z]       - start of string, must be a alpha character
        //    (?!.*--)        - look ahead and make sure there are no double hyphens (e.g., "--")
        //    [a-zA-Z0-9-]*   - match alphanumeric and hyphen as many times as needed
        //    [a-zA-Z0-9]     - final character must be alphanumeric
        if(!keyVaultName.matches((_keyVaultNameRegex))) {
            validSetting=false;
        }

        return validSetting;
    }

    public String getKeyVaultUri(){
        String kvUri = "";

        if (_keyVaultName != null && !_keyVaultName.isEmpty()){
            if(_keyVaultName.toUpperCase().startsWith("HTTPS://")){
                kvUri = _keyVaultName;
            }
            else{
                kvUri = "https://" + _keyVaultName + ".vault.azure.net";
            }
        }
        return kvUri;
    }

    public Mono<String> getSecret(String secretName){
        return Mono.create(sink -> {
            _keyVaultClient.getSecretAsync( getKeyVaultUri(), secretName, new ServiceCallback<SecretBundle>(){
            
                @Override
                public void success(SecretBundle secret) {
                    sink.success (secret.value());
                }
            
                @Override
                public void failure(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }

    // Returns null on error and logs error.
    // Created as a blocking function as app start-up is dependent on success.
    public List<SecretItem> listSecretsSync(){
        List<SecretItem> secrets=null;
        try{
            secrets = _keyVaultClient.listSecretsAsync(getKeyVaultUri(), null).get();
        }
        catch(Exception exception)
        {
            _logger.error(exception.getMessage());
        }

        return secrets;
    }

    // Created as a blocking function as app start-up is dependent on success.
    public Map<String, String> getSecretsSync(){
        List<SecretItem> secretItems = listSecretsSync();

        Map<String, String> secrets = new ConcurrentHashMap<String, String>();
        secretItems.forEach(item -> {
            String itemName = item.id().substring(item.id().lastIndexOf("/") + 1);
            String secretValue = getSecret(itemName).block();
            secrets.put(itemName, secretValue);
        });
        return secrets;
    }

    public Mono<KeyBundle> getKey (String keyName){
        return Mono.create(sink -> {
            _keyVaultClient.getKeyAsync(getKeyVaultUri(), keyName, new ServiceCallback<KeyBundle>() {
                @Override
                public void success(KeyBundle keyBundle) {
                    sink.success (keyBundle);
                }
            
                @Override
                public void failure(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }

    public Mono<CertificateBundle> getCertificate (String certName){
        return Mono.create (sink -> {
            _keyVaultClient.getCertificateAsync(getKeyVaultUri(), certName, new ServiceCallback<CertificateBundle>() {
                @Override
                public void success(CertificateBundle certBundle) {
                    sink.success (certBundle);
                }
            
                @Override
                public void failure(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }

}

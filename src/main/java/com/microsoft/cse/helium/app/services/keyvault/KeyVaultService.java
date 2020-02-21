package com.microsoft.cse.helium.app.services.keyvault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.AzureCliCredentials;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;

@Service
public class KeyVaultService implements IKeyVaultService
{
    private String _keyVaultName = "";
    private String _environmentFlag = "";
    private AzureTokenCredentials _credentials;
    private KeyVaultClient _keyVaultClient;

    private final String _keyVaultNameRegex ="^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";
    private static final Logger _logger = LoggerFactory.getLogger(KeyVaultService.class);

    public KeyVaultService (@Value("${helium.keyvault.name}") String keyVaultName, @Value("${helium.environment.flag}") String environmentFlag) throws Exception {
        _keyVaultName = keyVaultName.trim().toUpperCase();
        _environmentFlag = environmentFlag.trim().toUpperCase();

        // validate params and create credential object
        if(_environmentFlag == "MSI") {
            _credentials = new MSICredentials(AzureEnvironment.AZURE);
        } else if (_environmentFlag == "CLI") {
            try{
                _credentials = AzureCliCredentials.create();
            }
            catch (IOException ex)
            {
                _logger.error(ex.getMessage());
                throw(ex);
            }
        } else {
            throw new IllegalArgumentException("helium.environment.flag (value='" + _environmentFlag + "') must be 'MSI' or 'CLI'.  Check ${He_EnvironmentFlag} in your environment variables.");
        }
        
        if(!checkKeyVaultName(_keyVaultName)) {
            throw new IllegalArgumentException("helium.keyvault.name (value='" + _keyVaultName + "') must be 3-24 characters long, begin with a character, may contain alphanumeric or hyphen, no repeating hyphens, and end with alphanumeric.  Check ${KeyVaultName} in your environment variables.");
        }

        KeyVaultClient keyVaultClient = new KeyVaultClient(_credentials);

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
        String kvUri = "https://" + _keyVaultName + ".vault.azure.net";
        return kvUri;
    }

    public String getSecret(String secretName){
        String returnValue="";
        
        SecretBundle secret = _keyVaultClient.getSecret(getKeyVaultUri(), secretName);
        returnValue = secret.value();

        return returnValue;
    }

    public KeyBundle getKey (String keyName){

        return _keyVaultClient.getKey(getKeyVaultUri(), keyName);
    }

    public CertificateBundle getCertificate (String certName){

        return _keyVaultClient.getCertificate(getKeyVaultUri(), certName);

    }

}

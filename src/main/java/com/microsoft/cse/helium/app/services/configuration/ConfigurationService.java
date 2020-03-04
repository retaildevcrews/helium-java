package com.microsoft.cse.helium.app.services.configuration;

import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

import lombok.Getter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
// import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
// import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import org.springframework.context.annotation.Bean;

import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;

import reactor.core.publisher.Mono;

@Service
public class ConfigurationService implements IConfigurationService
{
    private static final Logger _logger = LoggerFactory.getLogger(ConfigurationService.class);

    //@Autowired
    private IKeyVaultService _keyVaultService;

    @Autowired
    public ConfigurationService(IKeyVaultService kvService){
        _keyVaultService = kvService;
        //retrieve keys and store in dictionary
        //String secretValue = _keyVaultService.getSecret("CosmosDbUrl").block();
        //List<String> = _keyVaultService.listSecrets().block();
        Dictionary<String, String> secrets = _keyVaultService.getSecrets();
        configEntries = secrets;
    }

    @Getter
    Dictionary configEntries = new Hashtable<String, String>();

    
    //@Bean
    /*
    public CosmosDBConfig getConfig() {
        RequestOptions options = getRequestOptions();
        ConnectionPolicy policy = new ConnectionPolicy();
        RetryOptions retryOptions = new RetryOptions();
        retryOptions.maxRetryWaitTimeInSeconds(60);
        policy.retryOptions(retryOptions);
        return CosmosDBConfig.builder(uri, key, dbName).requestOptions(options).connectionPolicy(policy).build();
    }
    */
    
}
package com.microsoft.cse.helium.app.config;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;

import com.azure.data.cosmos.ConsistencyLevel;
import com.azure.data.cosmos.ConnectionPolicy;
import com.azure.data.cosmos.RetryOptions;
import com.azure.data.cosmos.internal.RequestOptions;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableCosmosRepositories;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableReactiveCosmosRepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;

@Configuration

@EnableCosmosRepositories(basePackages = "com.microsoft.azure.helium.app.*")

public class CosmosDbConfig extends AbstractCosmosConfiguration {

    protected IConfigurationService _configService;
    protected String _cosmosContainer= "";
    protected String _cosmosDatabase= "";
    protected final RequestOptions _requestOptions = new RequestOptions();

    @Autowired
    public CosmosDbConfig(IConfigurationService configService){
        _configService = configService;
        _cosmosContainer = _configService.getConfigEntries().get(Constants.COSMOS_COLLECTION_KEYNAME);
        _cosmosDatabase = _configService.getConfigEntries().get(Constants.COSMOS_DATABASE_KEYNAME);

        _requestOptions.setConsistencyLevel(ConsistencyLevel.CONSISTENT_PREFIX);
        _requestOptions.setScriptLoggingEnabled(true);
    }

    @Bean
    @Primary
    public CosmosDBConfig buildCosmosDBConfig() {
        String uri = _configService.getConfigEntries().get(Constants.COSMOS_URL_KEYNAME);
        String key = _configService.getConfigEntries().get(Constants.COSMOS_KEY_KEYNAME);
        String dbName = _configService.getConfigEntries().get(Constants.COSMOS_DATABASE_KEYNAME);

        ConnectionPolicy policy = new ConnectionPolicy();
        RetryOptions retryOptions = new RetryOptions();
        retryOptions.maxRetryWaitTimeInSeconds(60);
        policy.retryOptions(retryOptions);
        return CosmosDBConfig.builder(uri, key, dbName).requestOptions(_requestOptions).connectionPolicy(policy).build();
    }
}
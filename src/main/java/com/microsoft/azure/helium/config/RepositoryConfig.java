package com.microsoft.azure.helium.config;


import com.azure.data.cosmos.ConnectionPolicy;
import com.azure.data.cosmos.ConsistencyLevel;
import com.azure.data.cosmos.RetryOptions;
import com.azure.data.cosmos.internal.RequestOptions;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * RepositoryConfig
 */
@Configuration
@EnableCosmosRepositories(basePackages = "com.microsoft.azure.helium.app.*")
public class RepositoryConfig extends AbstractCosmosConfiguration {

    @Value("${azure.cosmosdb.uri}")
    private  String uri;

    @Value("${azure.cosmosdb.key}")
    private String key;

    @Value("${azure.cosmosdb.database}")
    private  String dbName;

    private RequestOptions getRequestOptions() {
        final RequestOptions options = new RequestOptions();
        options.setConsistencyLevel(ConsistencyLevel.CONSISTENT_PREFIX);
        options.setScriptLoggingEnabled(true);
        return options;
    }

    @Bean
    public CosmosDBConfig getConfig() {
        RequestOptions options = getRequestOptions();
        ConnectionPolicy policy = new ConnectionPolicy();
        RetryOptions retryOptions = new RetryOptions();
        retryOptions.maxRetryWaitTimeInSeconds(60);
        policy.retryOptions(retryOptions);
        return CosmosDBConfig.builder(uri, key, dbName).requestOptions(options).connectionPolicy(policy).build();
    }
}

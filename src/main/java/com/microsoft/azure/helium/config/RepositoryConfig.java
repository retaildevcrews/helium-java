package com.microsoft.azure.helium.config;


import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import com.microsoft.azure.documentdb.*;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractDocumentDbConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.DocumentDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableDocumentDbRepositories;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * RepositoryConfig
 */
@Configuration
@EnableDocumentDbRepositories(basePackages = "com.microsoft.azure.helium.app.*")
public class RepositoryConfig extends AbstractDocumentDbConfiguration {

    @Value("${azure.cosmosdb.uri}")
    private  String uri;

    @Value("${azure.cosmosdb.key}")
    private String key;

    @Value("${azure.cosmosdb.database}")
    private  String dbName;

    @Override
    public DocumentDBConfig getConfig() {
        RequestOptions options = getRequestOptions();
        return DocumentDBConfig.builder(uri, key, dbName).requestOptions(options).build();
    }


    private RequestOptions getRequestOptions() {
        final RequestOptions options = new RequestOptions();
        options.setConsistencyLevel(ConsistencyLevel.ConsistentPrefix);
        options.setDisableRUPerMinuteUsage(true);
        options.setScriptLoggingEnabled(true);

        return options;
    }




}

package com.microsoft.cse.helium.app.dao;

import com.microsoft.cse.helium.app;
import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BaseDao{

    private static final Logger _logger = LoggerFactory.getLogger(BaseDao.class);
    
    private IConfigurationService _configService;
    private String _cosmosContainer= "";
    private String _cosmosDatabase= "";
    private final FeedOptions _feedOptions = new FeedOptions();

    @Autowired
    public BaseDao(IConfigurationService configService){
        _configService = configService;
        _cosmosContainer = _configService.getConfigEntries().get(Constants.COSMOS_COLLECTION_KEYNAME);
        _cosmosDatabase = _configService.getConfigEntries().get(Constants.COSMOS_DATABASE_KEYNAMEq);

        _feedOptions.enableCrossPartitionQuery(true);
        _feedOptions.maxDegreeOfParallelism(2);

    }


}
package com.microsoft.cse.helium.app.dao;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.services.configuration.*;

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.azure.data.cosmos.FeedOptions;

public class BaseCosmosDbDao{

    // private static final Logger _logger = LoggerFactory.getLogger(BaseCosmosDbDao.class);
    
    @Autowired
    ApplicationContext _context;

    protected IConfigurationService _configService;
    protected String _cosmosContainer= "";
    protected String _cosmosDatabase= "";
    protected final FeedOptions _feedOptions = new FeedOptions();

    @Autowired
    public BaseCosmosDbDao(IConfigurationService configService){
        _configService = configService;
        _cosmosContainer = _configService.getConfigEntries().get(Constants.COSMOS_COLLECTION_KEYNAME);
        _cosmosDatabase = _configService.getConfigEntries().get(Constants.COSMOS_DATABASE_KEYNAME);

        _feedOptions.enableCrossPartitionQuery(true);
        _feedOptions.maxDegreeOfParallelism(2);

    }

}
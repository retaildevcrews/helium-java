package com.microsoft.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosContainer;
import com.azure.data.cosmos.FeedOptions;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class BaseCosmosDbDao {

  @Autowired ApplicationContext context;

  @Autowired CommonUtils utils;

  protected IConfigurationService configurationService;
  protected String cosmosContainer = "";
  protected String cosmosDatabase = "";
  protected final FeedOptions feedOptions = new FeedOptions();

  private CosmosContainer container;

  /** BaseCosmosDbDao. */
  @Autowired
  public BaseCosmosDbDao(IConfigurationService configService) {
    configurationService = configService;
    cosmosContainer =
        configurationService.getConfigEntries().get(Constants.COSMOS_COLLECTION_KEYNAME);
    cosmosDatabase = configurationService.getConfigEntries().get(Constants.COSMOS_DATABASE_KEYNAME);

    feedOptions.enableCrossPartitionQuery(true);
    feedOptions.maxDegreeOfParallelism(Constants.MAX_DEGREE_PARALLELISM);
  }

  /** getContainer. */
  public CosmosContainer getContainer() {
    container =
        context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer);
    return container;
  }
}

package com.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosContainer;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.azure.data.cosmos.SqlQuerySpec;
import com.cse.helium.app.Constants;
import com.cse.helium.app.services.configuration.IConfigurationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

public class BaseCosmosDbDao {

  private static final Logger logger = LogManager.getLogger(BaseCosmosDbDao.class);

  @Autowired ApplicationContext context;

  protected IConfigurationService configurationService;
  protected String cosmosContainer = "";
  protected String cosmosDatabase = "";
  protected final FeedOptions feedOptions = new FeedOptions();

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
    return context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer);
  }

  /**
   * Common template method used to execute and retrieve queries for the given type using the passed
   * in query.
   *
   * @param classType used for the object mapper to map results into object
   * @param sqlQuerySpec is passed in from the specific data access object and used to fetch
   *     matching records
   * @return Flux/<T/> is returned to contains results for the specific entity type
   */
  public <T> Flux<T> getAll(Class<T> classType, SqlQuerySpec sqlQuerySpec) {
    ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();
    Flux<FeedResponse<CosmosItemProperties>> feedResponse =
        getContainer().queryItems(sqlQuerySpec, this.feedOptions);

    return feedResponse
                .flatMap(
                    flatFeedResponse -> Flux.fromIterable(flatFeedResponse.results()))
                .flatMap(
                    cosmosItemProperties -> {
                      try {
                        return Flux.just(
                            objMapper.readValue(cosmosItemProperties.toJson(), classType));
                      } catch (JsonProcessingException e) {
                        logger.error(e);
                      } 
                      return Flux.empty();
                    });
  }
}
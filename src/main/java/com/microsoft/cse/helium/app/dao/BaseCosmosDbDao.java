package com.microsoft.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosContainer;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
//import java.lang.reflect.ParameterizedType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

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

  /** getAMovies. */
  //@SuppressWarnings("unchecked")
  public <T> Flux<T> getAll(Class<T> classType, String query) {
    ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

    //logger.info("BaseCosmosDbDao::getAll::query:" + query);
    Flux<FeedResponse<CosmosItemProperties>> feedResponse =
        getContainer().queryItems(query, this.feedOptions);
    /*
    @SuppressWarnings("rawtypes")
    Class<T> classType = (Class) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    */
    Flux<T> selectedItems = (Flux<T>)feedResponse
        .flatMap(flatFeedResponse -> {
          return Flux.fromIterable(flatFeedResponse.results());
        }).flatMap(cosmosItemProperties -> {
          try {
            return Flux.just(objMapper.readValue(cosmosItemProperties.toJson(), classType));
          } catch (JsonMappingException e) {
            e.printStackTrace();
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
          return Flux.empty();
        });

    return selectedItems;
  }
}

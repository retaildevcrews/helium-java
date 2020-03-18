package com.microsoft.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorsDao extends BaseCosmosDbDao {
  private static final Logger logger = LoggerFactory.getLogger(ActorsDao.class);

  private static String actorSelect =
      "select m.id, m.partitionKey, m.actorId, m.type, "
          + "m.name, m.birthYear, m.deathYear, m.profession, "
          + "m.textSearch, m.movies from m where m.type = 'Actor' ";

  private static String actorContains = "and contains(m.textSearch, \"%s\") ";
  private static String actorOrderBy = " order by m.textSearch ";
  private static String actorOffset = " offset %d limit %d ";
  private static String actorSelectById = actorSelect + "and m.actorId = '%s'";

  public ActorsDao(IConfigurationService configService) {
    super(configService);
  }

  /** getActorById. */
  public Mono<Actor> getActorById(String actorId) {
    final String query = String.format(actorSelectById, actorId.toString());

    ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

    Mono<Actor> actor =
        this.context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer)
            .queryItems(query, this.feedOptions)
            .flatMap(
                cosmosItemFeedResponse ->
                    Mono.justOrEmpty(
                        cosmosItemFeedResponse.results().stream()
                            .map(cosmosItem -> cosmosItem.toObject(Actor.class))
                            .findFirst()))
            .onErrorResume(Mono::error)
            .next();
    return actor;
  }

  /** getActors. */
  public Flux<Actor> getActors(String query, Integer pageNumber, Integer pageSize) {
    ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

    String contains = "";

    if (query != null) {
      contains = String.format(actorContains, query);
    }

    String actorQuery =
        actorSelect + contains + actorOrderBy + String.format(actorOffset, pageNumber, pageSize);

    logger.info("actorQuery " + actorQuery);
    Flux<FeedResponse<CosmosItemProperties>> feedResponse =
        this.context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer)
            .queryItems(actorQuery, this.feedOptions);

    Flux<Actor> selectedActors =
        feedResponse
            .flatMap(
                flatFeedResponse -> {
                  return Flux.fromIterable(flatFeedResponse.results());
                })
            .flatMap(
                cosmosItemProperties -> {
                  try {
                    return Flux.just(
                        objMapper.readValue(cosmosItemProperties.toJson(), Actor.class));
                  } catch (JsonMappingException e) {
                    e.printStackTrace();
                  } catch (JsonProcessingException e) {
                    e.printStackTrace();
                  }
                  return Flux.empty();
                });

    return selectedActors;
  }
}

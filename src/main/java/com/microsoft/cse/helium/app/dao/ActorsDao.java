package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorsDao extends BaseCosmosDbDao implements IDao {
  private static final Logger logger = LoggerFactory.getLogger(ActorsDao.class);

  @Autowired CommonUtils utils;

  private static String actorSelect =
      "select m.id, m.partitionKey, m.actorId, m.type, "
          + "m.name, m.birthYear, m.deathYear, m.profession, "
          + "m.textSearch, m.movies from m where m.type = 'Actor' ";

  private static String actorContains = "and contains(m.textSearch, \"%s\") ";
  private static String actorOrderBy = " order by m.textSearch ";
  private static String actorOffset = " offset %d limit %d ";

  /** ActorsDao. */
  public ActorsDao(IConfigurationService configService) {
    super(configService);
  }

  /** getActorByIdSingleRead. */
  public Mono<Actor> getActorById(String actorId) {
    Mono<Actor> actor =
        getContainer()
            .getItem(actorId, utils.getPartitionKey(actorId))
            .read()
            .flatMap(
                cosmosItemResponse -> {
                  return Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Actor.class));
                })
            .onErrorResume(throwable -> findAPIExceptionHandler("Failed to find item", throwable));
    return actor;
  }

  /** getActors. */
  public Flux<Actor> getAll(String query, Integer pageNumber, Integer pageSize) {

    String contains = "";

    if (query != null) {
      contains = String.format(actorContains, query);
    }

    String actorQuery =
        actorSelect + contains + actorOrderBy + String.format(actorOffset, pageNumber, pageSize);

    logger.info("actorQuery " + actorQuery);
    Flux<Actor> queryResult = super.getAll(Actor.class, actorQuery);
    return queryResult;
  }
}

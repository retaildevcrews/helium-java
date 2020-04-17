package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import java.util.Map;
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
  private static String actorOrderBy = " order by m.textSearch, m.actorId ";
  private static String actorOffset = " offset %d limit %d ";

  /** ActorsDao. */
  public ActorsDao(IConfigurationService configService) {
    super(configService);
  }

  /** getActorByIdSingleRead. */
  public Mono<Actor> getActorById(String actorId) {
    logger.info("Call to getActorById (" + actorId + ")");
    Mono<Actor> actor =
        getContainer()
            .getItem(actorId, utils.getPartitionKey(actorId))
            .read()
            .flatMap(
                cosmosItemResponse -> {
                  return
                      Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Actor.class));
                })
            .onErrorResume(throwable ->
                findAPIExceptionHandler("Failed to find item", throwable));
    return actor;
  }

  /**
   * This method is responsible for checking for expected values in the queryParams dictionary
   * validating them, building the query, and then passing to the base getAll() implementation.
   *
   * @param queryParams for actors this is a single query value stored in the key "q"
   * @param pageNumber used to specify which page of the paginated results to return
   * @param pageSize used to specify the number of results per page
   * @return Flux/<T/> is returned to contains results for the specific entity type
   */
  public Flux<?> getAll(Map<String, Object> queryParams, Integer pageNumber, Integer pageSize) {
    String contains = "";
    String query = null;

    if (queryParams.containsKey("q")) { 
      query = queryParams.get("q").toString();
    }

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

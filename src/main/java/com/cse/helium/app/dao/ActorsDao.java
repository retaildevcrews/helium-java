package com.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.azure.data.cosmos.SqlParameter;
import com.azure.data.cosmos.SqlParameterList;
import com.azure.data.cosmos.SqlQuerySpec;
import com.cse.helium.app.Constants;
import com.cse.helium.app.models.Actor;
import com.cse.helium.app.services.configuration.IConfigurationService;
import com.cse.helium.app.utils.CommonUtils;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorsDao extends BaseCosmosDbDao implements IDao {
  private static final Logger logger = LogManager.getLogger(ActorsDao.class);

  @Autowired CommonUtils utils;

  private static String actorSelect =
      "select m.id, m.partitionKey, m.actorId, m.type, "
          + "m.name, m.birthYear, m.deathYear, m.profession, "
          + "m.textSearch, m.movies from m where m.type = @type ";

  private static String actorContains = "and contains(m.textSearch, @query) ";
  private static String actorOrderBy = " order by m.textSearch, m.actorId ";
  private static String actorOffset = " offset @offset limit @limit ";

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
                  return Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Actor.class));
                })
            .onErrorResume(throwable -> findAPIExceptionHandler("Failed to find item", throwable));
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
    String query = null;

    if (queryParams.containsKey("q")) {
      query = queryParams.get("q").toString();
    }

    final String actorDocumentType = Constants.ACTOR_DOCUMENT_TYPE;
    final SqlQuerySpec actorQuerySpec = new SqlQuerySpec();

    SqlParameterList parameterList = new SqlParameterList();
    parameterList.add(new SqlParameter("@type", actorDocumentType));
    parameterList.add(new SqlParameter("@offset", pageNumber));
    parameterList.add(new SqlParameter("@limit", pageSize));

    StringBuilder queryBuilder = new StringBuilder(actorSelect);
    if (query != null) {
      queryBuilder.append(actorContains);
      parameterList.add(new SqlParameter("@query", query));
    }
    queryBuilder.append(actorOrderBy).append(actorOffset);

    actorQuerySpec.queryText(queryBuilder.toString());
    actorQuerySpec.parameters(parameterList);

    Flux<Actor> queryResult = super.getAll(Actor.class, actorQuerySpec);

    return queryResult;
  }
}

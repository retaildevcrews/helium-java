package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.microsoft.cse.helium.app.models.Movie;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesDao extends BaseCosmosDbDao implements IDao {
  private static final Logger logger = LoggerFactory.getLogger(MoviesDao.class);

  @Autowired CommonUtils utils;
  @Autowired GenresDao genresDao;

  private static String movieSelect =
      "select m.id, m.partitionKey, m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime,"
          + "m.rating, m.votes, m.totalScore, m.genres, m.roles from m where m.type = 'Movie'  ";

  private static String movieContains = "and contains(m.textSearch, \"%s\") ";
  private static String movieOrderBy = " order by m.textSearch ";
  private static String movieOffset = " offset %d limit %d ";

  /** MoviesDao. */
  public MoviesDao(IConfigurationService configService) {
    super(configService);
  }

  /** getMovieByIdSingleRead. */
  public Mono<Movie> getMovieById(String movieId) {
    Mono<Movie> movie =
        getContainer()
            .getItem(movieId, utils.getPartitionKey(movieId))
            .read()
            .flatMap(
                cosmosItemResponse -> {
                  return Mono.justOrEmpty(cosmosItemResponse.properties()
                      .toObject(Movie.class));
                })
            .onErrorResume(throwable ->
                findAPIExceptionHandler("Failed to find item", throwable));
    return movie;
  }

  /*
  public Flux<Movie> getAll(
      String query, String genre, Integer year,
      Integer rating, String actorId, Integer pageNumber, Integer pageSize) {
    StringBuilder formedQuery = new StringBuilder(movieSelect);

    String contains = "";
    if (query != null) {
      contains = String.format(movieContains, query);
      formedQuery.append(contains);
    }

    String yearSelect = "";
    if (year > 0) {
      yearSelect = " and m.year = " + year;
      formedQuery.append(yearSelect);
    }

    String ratingSelect = "";
    if (rating > 0) {
      ratingSelect = " and m.rating >= " + rating;
      formedQuery.append(ratingSelect);
    }

    String actorSelect = "";
    if (!StringUtils.isEmpty(actorId)) {
      actorSelect = " and array_contains(m.roles, { actorId: '" + actorId + "' }, true) ";
      formedQuery.append(actorSelect);
    }

    String moviesQuery =
        formedQuery
            .append(movieOrderBy)
            .append(String.format(movieOffset, pageNumber, pageSize))
            .toString();

    logger.info("Movies query = " + moviesQuery);

    Flux<Movie> queryResult = super.getAll(Movie.class, moviesQuery);
    return queryResult;
  }
  */
  
  /**
  * This method is responsible for checking for expected values in the queryParams dictionary
  * validating them, building the query, and then passing to the base getAll() implementation.
  *
  * @param queryParams dictionary my contain "q", "year", "ratingSelect", and "actorSelect"
  * @param pageNumber used to specify which page of the paginated results to return
  * @param pageSize used to specify the number of results per page
  * @return Flux/<T/> is returned to contains results for the specific entity type
  */
  public Flux<?> getAll(Map<String, String> queryParams, Integer pageNumber, Integer pageSize) {
    StringBuilder formedQuery = new StringBuilder(movieSelect);
    
    String contains = "";
    if (queryParams.containsKey("q")) { 
      contains = String.format(movieContains, queryParams.get("q"));
      formedQuery.append(contains);
    }

    String yearSelect = "";
    if (queryParams.containsKey("year")) { 
      Integer year = queryParams.get("year");
      if (year > 0) {
        yearSelect = " and m.year = " + year;
        formedQuery.append(yearSelect);
      }
    }

    String ratingSelect = "";
    if (queryParams.containsKey("ratingSelect")) {
      Integer rating = queryParams.get("ratingSelect");
      if (rating > 0) {
        ratingSelect = " and m.rating >= " + rating;
        formedQuery.append(ratingSelect);
      }
    }

    String actorSelect = "";
    if (queryParams.containsKey("actorSelect")) {
      String actorId = queryParams.get("actorSelect");
      if (!StringUtils.isEmpty(actorId)) {
        actorSelect = " and array_contains(m.roles, { actorId: '" + actorId + "' }, true) ";
        formedQuery.append(actorSelect);
      }
    }

    String moviesQuery =
        formedQuery
            .append(movieOrderBy)
            .append(String.format(movieOffset, pageNumber, pageSize))
            .toString();

    logger.info("Movies query = " + moviesQuery);

    Flux<Movie> queryResult = super.getAll(Movie.class, moviesQuery);
    return queryResult;
  }
  
}

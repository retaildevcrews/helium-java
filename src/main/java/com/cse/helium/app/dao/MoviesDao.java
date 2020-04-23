package com.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.cse.helium.app.models.Movie;
import com.cse.helium.app.services.configuration.IConfigurationService;
import com.cse.helium.app.utils.CommonUtils;
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
  private static String movieOrderBy = " order by m.textSearch, m.movieId ";
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

  /**
  * This method is responsible for checking for expected values in the queryParams dictionary
  * validating them, building the query, and then passing to the base getAll() implementation.
  *
  * @param queryParams dictionary my contain "q", "year", "ratingSelect", and "actorSelect"
  * @param pageNumber used to specify which page of the paginated results to return
  * @param pageSize used to specify the number of results per page
  * @return Flux/<T/> is returned to contains results for the specific entity type
  */
  public Flux<?> getAll(Map<String, Object> queryParams, Integer pageNumber, Integer pageSize) {
    StringBuilder formedQuery = new StringBuilder(movieSelect);


    String contains = "";
    if (queryParams.containsKey("q")) {
      contains = String.format(movieContains, queryParams.get("q"));
      formedQuery.append(contains);
    }

    String yearSelect = "";
    if (queryParams.containsKey("year")) {
      Integer year = (Integer) queryParams.get("year");
      if (year > 0) {
        yearSelect = " and m.year = " + year;
        formedQuery.append(yearSelect);
      }
    }

    String ratingSelect = "";
    if (queryParams.containsKey("ratingSelect")) {
      Double rating = (Double) queryParams.get("ratingSelect");
      if (rating > 0.0) {
        ratingSelect = " and m.rating >= " + rating;
        formedQuery.append(ratingSelect);
      }
    }

    String actorSelect = "";
    if (queryParams.containsKey("actorSelect")) {
      String actorId = queryParams.get("actorSelect").toString();
      if (!StringUtils.isEmpty(actorId)) {
        actorSelect = " and array_contains(m.roles, { actorId: '" + actorId + "' }, true) ";
        formedQuery.append(actorSelect);
      }
    }

    //special genre call to support webflux chain
    if (queryParams.containsKey("genre")) {
      String genre = queryParams.get("genre").toString();
      if (!StringUtils.isEmpty(genre)) {
        return filterByGenre(genre, formedQuery.toString(), pageNumber, pageSize);
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

  /** filterByGenre. */
  public Flux<Movie> filterByGenre(String genreKey, String query, Integer pageNumber,
                                   Integer pageSize) {
    return
        genresDao
            .getGenreByKey(genreKey)
            .collectList()
            .flatMapMany(selectedGenre -> {
              String genreSelect = " and array_contains(m.genres,'" + selectedGenre.get(0) + "')";
              StringBuilder movieQuery =
                  new StringBuilder(query)
                      .append(genreSelect)
                      .append(movieOrderBy)
                      .append(String.format(movieOffset, pageNumber, pageSize));

              logger.info("Movies query = " + movieQuery.toString());

              return super.getAll(Movie.class, movieQuery.toString());
            });
  }

}

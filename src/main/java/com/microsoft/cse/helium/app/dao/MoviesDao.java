package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.microsoft.cse.helium.app.models.Movie;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import org.apache.commons.lang3.NotImplementedException;
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
                  return Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Movie.class));
                })
            .onErrorResume(throwable -> findAPIExceptionHandler("Failed to find item", throwable));
    return movie;
  }

  /** getAllMovies. */
  public Flux<Movie> getAll(
      String query, String genre, Integer year, Integer pageNumber, Integer pageSize) {
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

    String moviesQuery =
        formedQuery
            .append(movieOrderBy)
            .append(String.format(movieOffset, pageNumber, pageSize))
            .toString();

    logger.info("Movies query = " + moviesQuery);

    Flux<Movie> queryResult = super.getAll(Movie.class, moviesQuery);
    return queryResult;
  }

  public Flux<?> getAll(String query, Integer pageNumber, Integer pageSize) {
    throw new NotImplementedException("Not Implemented");
  }
}

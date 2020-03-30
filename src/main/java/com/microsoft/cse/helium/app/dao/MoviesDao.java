package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;
/*
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
*/

import com.microsoft.cse.helium.app.models.Movie;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.cse.helium.app.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesDao extends BaseCosmosDbDao {
  private static final Logger logger = LoggerFactory.getLogger(MoviesDao.class);

  @Autowired CommonUtils utils;

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

  /** getAMovies. */
  public Flux<Movie> getMovies(String query, Integer pageNumber, Integer pageSize) {

    String contains = "";

    if (query != null) {
      contains = String.format(movieContains, query);
    }

    String moviesQuery =
        movieSelect + contains + movieOrderBy + String.format(movieOffset, pageNumber, pageSize);
    logger.info("Movies query = " + moviesQuery);
    Movie movie = new Movie();
    Flux<Movie> testBaseResult = this.getAll(movie, moviesQuery);
    return testBaseResult;
    /*
    ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

    logger.info("movieQuery " + moviesQuery);
    Flux<FeedResponse<CosmosItemProperties>> feedResponse =
        getContainer().queryItems(moviesQuery, this.feedOptions);

    Flux<Movie> selectedMovies =
        feedResponse
            .flatMap(
                flatFeedResponse -> {
                  return Flux.fromIterable(flatFeedResponse.results());
                })
            .flatMap(
                cosmosItemProperties -> {
                  try {
                    return Flux.just(
                        objMapper.readValue(cosmosItemProperties.toJson(), Movie.class));
                  } catch (JsonMappingException e) {
                    e.printStackTrace();
                  } catch (JsonProcessingException e) {
                    e.printStackTrace();
                  }
                  return Flux.empty();
                });

    return selectedMovies;
    */
  }
}

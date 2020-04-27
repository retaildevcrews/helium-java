package com.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.cse.helium.app.models.Genre;
import com.cse.helium.app.services.configuration.IConfigurationService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenresDao extends BaseCosmosDbDao {
  private static final Logger logger =   LogManager.getLogger(GenresDao.class);

  private static String genreQuery =
      "select m.genre from m where m.type = 'Genre' order by m.genre ";

  private static String genreQueryById = "select m.genre from m where m.type = 'Genre' and m.id = ";


  public GenresDao(IConfigurationService configService) {
    super(configService);
  }

  /**
   * getActors.
   *
   * @return
   */
  public Mono<List<String>> getGenres() {
    logger.info("genreQuery " + genreQuery);

    Mono<List<String>> selectedGenres =
        this.context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer)
            .queryItems(genreQuery, this.feedOptions)
            .flatMap(
                flatFeedResponse -> {
                  return Flux.fromIterable(flatFeedResponse.results());
                })
            .map(cosmosItemProperties ->
                cosmosItemProperties.toObject(Genre.class).getGenre())
            .collectList();

    return selectedGenres;
  }

  /** getGenreByKey. */
  public Flux<String> getGenreByKey(String genreKey) {

    String genreQuery = genreQueryById + "'" + genreKey.toLowerCase() + "'";
    logger.info("genreQuery " + genreQuery);

    Flux<String> genreFlux =
        getContainer()
            .queryItems(genreQuery, this.feedOptions)
            .flatMap(
                cosmosItemResponse -> {
                  return Flux.fromIterable(cosmosItemResponse.results());
                })
            .map(cosmosItemProperties -> cosmosItemProperties.toObject(Genre.class))
            .map(selectedGenre -> selectedGenre.getGenre());
    return genreFlux;
  }


}

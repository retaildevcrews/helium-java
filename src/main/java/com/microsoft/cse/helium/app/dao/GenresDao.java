package com.microsoft.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.azure.data.cosmos.CosmosClient;
import com.microsoft.cse.helium.app.models.Genre;
import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenresDao extends BaseCosmosDbDao {
  private static final Logger logger = LoggerFactory.getLogger(GenresDao.class);

  private static String genreQuery =
      "select m.genre from m where m.type = 'Genre' order by m.genre ";

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

  /**
   * getGenreByKey.
   *
   * @return
   */
  public Mono<Genre> getGenreByKey(String key) {
    logger.info("key " + key);
    Mono<Genre> genreMono =
        getContainer()
            .getItem(key.toLowerCase(), "0")
            .read()
            .flatMap(
                cosmosItemResponse -> {
                  return Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Genre.class));
                });

/*    genreMono.subscribe(s->{
      System.out.println("am i here " + s.getGenre());
    }); */

    logger.info("completed");
    return genreMono;
  }




}

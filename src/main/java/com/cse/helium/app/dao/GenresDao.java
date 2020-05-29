package com.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.SqlParameter;
import com.azure.data.cosmos.SqlParameterList;
import com.azure.data.cosmos.SqlQuerySpec;
import com.cse.helium.app.Constants;
import com.cse.helium.app.models.Genre;
import com.cse.helium.app.services.configuration.IConfigurationService;
import java.text.MessageFormat;
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

  private static String genreQueryById =
      "select m.genre from m where m.type = @id and m.id = @type";

  public GenresDao(IConfigurationService configService) {
    super(configService);
  }

  /**
   * getActors.
   *
   * @return
   */
  public Mono<List<String>> getGenres() {
    logger.info(MessageFormat.format("genreQuery {0}", genreQuery));

    SqlQuerySpec sqsGenreQuery = new SqlQuerySpec(genreQuery);

    return this.context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer)
            .queryItems(sqsGenreQuery, this.feedOptions)
            .flatMap(
                flatFeedResponse -> Flux.fromIterable(flatFeedResponse.results()))
            .map(cosmosItemProperties -> cosmosItemProperties.toObject(Genre.class).getGenre())
            .collectList();
  }

  /** getGenreByKey. */
  public Flux<String> getGenreByKey(String genreKey) {
    SqlQuerySpec sqsGenreQueryById =
        new SqlQuerySpec(
            genreQueryById,
            new SqlParameterList(
                new SqlParameter("@id", Constants.GENRE_DOCUMENT_TYPE),
                new SqlParameter("@type", genreKey.toLowerCase())));

    return getContainer()
            .queryItems(sqsGenreQueryById, this.feedOptions)
            .flatMap(
                cosmosItemResponse -> Flux.fromIterable(cosmosItemResponse.results()))
            .map(cosmosItemProperties -> cosmosItemProperties.toObject(Genre.class))
            .map(selectedGenre -> selectedGenre.getGenre());
  }
}

package com.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.cse.helium.app.models.FeaturedMovie;
import com.cse.helium.app.services.configuration.IConfigurationService;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class FeaturedMovieDao extends BaseCosmosDbDao {
  private static String featuredMovieQuery =
      "select m.movieId, m.weight from m where m.type = 'Featured' ";
  @Autowired MoviesDao moviesDao;

  public FeaturedMovieDao(IConfigurationService configService) {
    super(configService);
  }

  /**
   * getFeaturedMovie.
   *
   * @return
   */
  public Flux<String> getFeaturedMovie() {

    return this.context
            .getBean(CosmosClient.class)
            .getDatabase(this.cosmosDatabase)
            .getContainer(this.cosmosContainer)
            .queryItems(featuredMovieQuery, this.feedOptions)
            .flatMap(
                flatFeedResponse -> Flux.fromIterable(flatFeedResponse.results()))
            .map(cosmosItemProperties -> cosmosItemProperties.toObject(FeaturedMovie.class))
            .sort(Comparator.comparing(FeaturedMovie::getWeight))
            .map(featuredMovie -> featuredMovie.getMovieId());
  }
}

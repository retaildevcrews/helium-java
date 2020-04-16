package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.dao.FeaturedMovieDao;
import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.models.Movie;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


/** FeaturedController. */
@RestController
@RequestMapping(path = "/api/featured/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeaturedController {
  private static final Logger logger = LoggerFactory.getLogger(FeaturedController.class);

  @Autowired FeaturedMovieDao featuredMovieDao;
  @Autowired MoviesDao moviesDao;

  /** getFeaturedMovies. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Movie> getFeaturedMovies() {

    try {
      return featuredMovieDao
          .getFeaturedMovie()
          .collectList()
          .flatMap(
              featuredMovies -> {
                int randomNum = new Random().nextInt(((featuredMovies.size() - 1) - 0) + 1) + 0;
                return moviesDao.getMovieById(featuredMovies.get(randomNum));
              });
    } catch (Exception ex) {

      logger.error("Error received in FeaturedController", ex);
      return Mono.error(new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "featured Error"));
    }
  }
}

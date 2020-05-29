package com.cse.helium.app.controllers;

import com.cse.helium.app.dao.FeaturedMovieDao;
import com.cse.helium.app.dao.MoviesDao;
import com.cse.helium.app.models.Movie;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


/** FeaturedController. */
@RestController
@RequestMapping(path = "/api/featured/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeaturedController {
  private static final Logger logger =   LogManager.getLogger(FeaturedController.class);

  @Autowired FeaturedMovieDao featuredMovieDao;
  @Autowired MoviesDao moviesDao;

  /** getFeaturedMovies. */
  @GetMapping(
      value = "",
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

package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.models.Movie;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** MovieController. */
@RestController
@RequestMapping(path = "/api/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Movies")
public class MoviesController {
  @Autowired MoviesDao moviesDao;
  @Autowired ParameterValidator validator;

  private static final Logger logger = LoggerFactory.getLogger(MoviesController.class);

  /** getMovie. */
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(
      value = "Get single movie",
      notes = "Retrieve and return a single movie by movie Id")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "The movie object"),
        @ApiResponse(code = 404, message = "An Movie with the specified ID was not found")
      })
  public Mono<ResponseEntity<Movie>> getMovie(
      @ApiParam(value = "The ID of the movie to look for", example = "tt0000002", required = true)
          @PathVariable("id")
          String movieId) {
    if (validator.isValidMovieId(movieId)) {
      return moviesDao
          .getMovieById(movieId)
          .map(foundMovie -> ResponseEntity.ok(foundMovie))
          .defaultIfEmpty(ResponseEntity.notFound().build());
    } else {
      logger.error("Movie not found with Id: " + movieId);
      return Mono.justOrEmpty(
          ResponseEntity.badRequest()
              .contentType(MediaType.TEXT_PLAIN)
              .header("Movie not found with Id: " + movieId)
              .build());
    }
  }
}

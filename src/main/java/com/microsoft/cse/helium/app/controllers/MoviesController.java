package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.models.Entity;
import com.microsoft.cse.helium.app.models.Movie;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** MovieController. */
@RestController
@RequestMapping(path = "/api/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Movies")
public class MoviesController extends Controller {
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
          .map(savedMovie -> ResponseEntity.ok(savedMovie))
          .defaultIfEmpty(ResponseEntity.notFound().build());
    } else {
      logger.error("Invalid movieId parameter " + movieId);
      return Mono.justOrEmpty(
          ResponseEntity.badRequest()
              .contentType(MediaType.TEXT_PLAIN)
              .header("Invalid movieId parameter")
              .build());
    }
  }

  /** getAllMovies. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all movies", notes = "Retrieve and return all movies")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List of movie objects")})
  public Object getAllMovies(
      @ApiParam(value = "(query) (optional) The term used to search Movie name") @RequestParam("q")
          final Optional<String> query,
      @ApiParam(value = "1 based page index", defaultValue = "1") @RequestParam
          Optional<String> pageNumber,
      @ApiParam(value = "page size (1000 max)", defaultValue = "100") @RequestParam
          Optional<String> pageSize) {

    return getAll(query, pageNumber, pageSize, Entity.Movie);
  }
}

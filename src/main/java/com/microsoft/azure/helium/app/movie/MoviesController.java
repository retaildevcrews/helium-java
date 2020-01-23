package com.microsoft.azure.helium.app.movie;

import java.util.List;
import java.util.Optional;

import com.azure.data.cosmos.CosmosClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * MovieController
 */
@RestController
@RequestMapping(path = "/api/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Movies")
public class MoviesController {

    @Autowired
    private MoviesService service;

  //https://github.com/springfox/springfox/issues/2418 - cannot order API Params
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all movies by filters", notes = "Retrieve and return all movies")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List of movie objects") })
    public ResponseEntity<List<Movie>> getAllMoviesByFilters(
            @ApiParam(value = "(query) (optional) The term used to search by movie title (rings)", required = false ) @RequestParam final Optional<String> q,
            @ApiParam(value = "(optional) Movies of a genre (Action)", required = false ) @RequestParam final Optional<String> genre,
            @ApiParam(value = "(optional) Get movies by year (2005)", required = false , defaultValue = "0") @RequestParam final Optional<Integer> year,
            @ApiParam(value = "(optional) Get movies with a rating >= rating (8.5)", required = false , defaultValue = "0") @RequestParam final Optional<Double> rating,
            @ApiParam(value = "(optional) Get top rated movies (true)", required = false , defaultValue = "0") @RequestParam final Optional<Boolean> toprated,
            @ApiParam(value = "(optional) Get movies by Actor Id (nm0000704)", required = false) @RequestParam final Optional<String> actorid,
            @ApiParam(value = "0 based page index", required = false , defaultValue = "0") @RequestParam Optional<Integer>  pagenumber,
            @ApiParam(value = "page size (1000 max)", required = false , defaultValue = "100") @RequestParam Optional<Integer>  pagesize
    ) throws CosmosClientException {

        final Sort sort = Sort.by(Sort.Direction.ASC, "movieId");

        List<Movie> movies = service.getAllMovies(q, genre, year, rating, toprated, actorid, pagesize, pagenumber);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get single movie", notes = "Retrieve and return a single movie by movie ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The movie object"),
            @ApiResponse(code = 404, message = "A movie with the specified ID was not found") })
    public ResponseEntity<Movie> getMovie(
            @ApiParam(value = "The ID of the movie to look for", required = true, example = "tt0120737") @PathVariable("id") final String movieId) {
        Optional<Movie> movie = service.getMovie(movieId);
        if (movie.isPresent()) {
            return new ResponseEntity<>(movie.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
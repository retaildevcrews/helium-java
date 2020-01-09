package com.microsoft.azure.helium.app.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all movies", notes = "Retrieve and return all movies")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List of movie objects") })
    public ResponseEntity<List<Movie>> getAllMovies(
            @ApiParam(value = "The movie title to filter by", required = false)
            @RequestParam("q") final Optional<String> query,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "100") Integer pageSize
            ) {

        final Sort sort = new Sort(Sort.Direction.ASC, "movieId");
        List<Movie> movies = service.getAllMovies(query, pageNo, pageSize, sort);
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
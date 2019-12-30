package com.microsoft.azure.helium.app.genre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * GenreController
 */
@RestController
@RequestMapping(path = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Genres")
public class GenresController {

    @Autowired
    private GenresService service;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all genres", notes = "Retrieve and return all genres")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List of genres objects") })
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = service.getAllGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }
}
package com.microsoft.azure.helium.app.featured;
import com.microsoft.azure.helium.app.movie.Movie;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * FeaturedController
 */
@RestController
@RequestMapping(path = "/api/featured", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Featured")
public class FeaturedController {

    @Autowired
    private FeaturedService service;

    @RequestMapping(value = "/movie", method = RequestMethod.GET)
    @ApiOperation(value = "Get featured movie", notes = "Get list of featured movies")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "list of featured movies") })
    public ResponseEntity<Movie> getFeaturedMovie(
    ) {
        Movie movie = service.getFeaturedMovie();
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

}

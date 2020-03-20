package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.dao.GenresDao;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** ActorController. */
@RestController
@RequestMapping(path = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Genres")
public class GenresController {
  @Autowired GenresDao genresDao;
  @Autowired ParameterValidator validator;

  /** getAllGenres. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all genres", notes = "Retrieve and return all genres")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List of actor objects")})
  public Mono<List<String>> getAllGenres() {
    return genresDao.getGenres();
  }
}

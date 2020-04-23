package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.dao.GenresDao;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/** ActorController. */
@RestController
@RequestMapping(path = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Genres")
public class GenresController {
  private static final Logger logger =   LogManager.getLogger(GenresController.class);

  @Autowired GenresDao genresDao;
  @Autowired ParameterValidator validator;

  /** getAllGenres. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<String>> getAllGenres() {
    try {
      return genresDao.getGenres();
    } catch (Exception ex) {

      logger.error("Error received in GenresController", ex);
      return Mono.error(new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "genres Error"));
    }
  }
}

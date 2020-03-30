package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** ActorController. */
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController extends Controller {

  @Autowired ActorsDao actorsDao;

  @Autowired ParameterValidator validator;

  private static final Logger logger = LoggerFactory.getLogger(ActorsController.class);

  /** getActor. */
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<Actor>> getActor(
      @ApiParam(value = "The ID of the actor to look for", example = "nm0000002", required = true)
      @PathVariable("id")
          String actorId) {
    if (validator.isValidActorId(actorId)) {
      return actorsDao
          .getActorById(actorId)
          .map(savedActor -> ResponseEntity.ok(savedActor))
          .defaultIfEmpty(ResponseEntity.notFound().build());
    } else {
      logger.error("Invalid Actor ID parameter " + actorId);
      return Mono.justOrEmpty(
          ResponseEntity.badRequest()
              .contentType(MediaType.TEXT_PLAIN)
              .header("Invalid Actor ID parameter")
              .build());
    }
  }

  /** getAllActors. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Object getAllActors(
      @ApiParam(value = "(query) (optional) The term used to search Actor name") @RequestParam("q")
      final Optional<String> query,
      @ApiParam(value = "1 based page index", defaultValue = "1") @RequestParam
          Optional<String> pageNumber,
      @ApiParam(value = "page size (1000 max)", defaultValue = "100") @RequestParam
          Optional<String> pageSize) {

    try {
      return getAll(query, pageNumber, pageSize, actorsDao);
    } catch (Exception ex) {
      logger.error("ActorControllerException " + ex.getMessage());
      return new ResponseEntity<>(
          Constants.ACTOR_CONTROLLER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

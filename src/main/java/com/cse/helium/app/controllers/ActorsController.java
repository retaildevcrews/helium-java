package com.cse.helium.app.controllers;

import com.cse.helium.app.Constants;
import com.cse.helium.app.dao.ActorsDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.text.MessageFormat;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** ActorController. */
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController extends Controller {

  private static final Logger logger =   LogManager.getLogger(ActorsController.class);

  @Autowired ActorsDao dao;

  /** getActor. */
  @GetMapping(
      value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  // to suprress wrapping the logger.error() in a conditional and lambda to function
  @SuppressWarnings({"squid:S2629", "squid:S1612"})
  public Object getActor(
      @ApiParam(value = "The ID of the actor to look for", example = "nm0000002", required = true)
      @PathVariable("id")
          String actorId) {

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("getActor (actorId={0})",actorId));
    }

    if (Boolean.TRUE.equals(validator.isValidActorId(actorId))) {
      return dao
          .getActorById(actorId)
          .doOnSuccess(value -> logger.info("!!!!! completed actorsDao call!!!!"))
          .switchIfEmpty(Mono.defer(() ->
            Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Not Found"))));
    } else {
      logger.error(MessageFormat.format("Invalid Actor ID parameter {0}", actorId));

      return new ResponseEntity<String>(Constants.INVALID_ACTORID_MESSAGE,
          HttpStatus.BAD_REQUEST);
    }
  }

  /** getAllActors. */
  @GetMapping(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Object getAllActors(
      @ApiParam(value = "(query) (optional) The term used to search Actor name") @RequestParam("q")
      final Optional<String> query,
      @ApiParam(value = "1 based page index", defaultValue = "1") @RequestParam
          Optional<String> pageNumber,
      @ApiParam(value = "page size (1000 max)", defaultValue = "100") @RequestParam
          Optional<String> pageSize) {

    try {
      if (logger.isInfoEnabled()) {
        logger.info(MessageFormat.format("getAllActors (query={0}, pageNumber={1}, pageSize={2})",
            query, pageNumber, pageSize));
      }

      return super.getAll(query,pageNumber, pageSize, dao);
    } catch (Exception ex) {
      logger.error(MessageFormat.format("ActorControllerException {0}", ex.getMessage()));
      return Flux.error(new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, Constants.ACTOR_CONTROLLER_EXCEPTION));
    }
  }
}

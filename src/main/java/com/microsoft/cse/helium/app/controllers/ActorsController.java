package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.utils.Validator;
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
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ActorController.
 */
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController {
  @Autowired
  ActorsDao actorsDao;
  @Autowired
  Validator validator;

  private static final Logger logger = LoggerFactory.getLogger(ActorsController.class);

  /**
   * getActor.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get single actor",
      notes = "Retrieve and return a single actor by actor ID")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "The actor object"),
      @ApiResponse(code = 404, message = "An actor with the specified ID was not found")})
  public Mono<ResponseEntity<Actor>> getActor(
      @ApiParam(value = "The ID of the actor to look for", example = "nm0000002", required = true)
      @PathVariable("id") String actorId) {

    if (validator.isValidActorId(actorId)) {
      return actorsDao.getActorById(actorId)
          .map(savedActor -> ResponseEntity.ok(savedActor))
          .defaultIfEmpty(ResponseEntity.notFound().build());
    } else {
      logger.error("Invalid actorId parameter " + actorId);
      return Mono.justOrEmpty(ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
          .build());
    }

  }

  /**
   * getAllActors.
   */
  @RequestMapping(value = "", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all actors", notes = "Retrieve and return all actors")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List of actor objects")})
  public Flux<Actor> getAllActors(
      @ApiParam(value = "(query) (optional) The term used to search Actor name", required = false)
      @RequestParam final Optional<String> q,
      @RequestParam("q") final Optional<String> query,
      @ApiParam(value = "0 based page index", defaultValue = "0")
      @RequestParam Optional<Integer> pageNumber,
      @ApiParam(value = "page size (1000 max)", defaultValue = "100")
      @RequestParam Optional<Integer> pageSize,
      ServerHttpResponse response) {
    Integer tmpPageNumber = 0;
    Integer tmpPageSize = Constants.DEFAULT_PAGE_SIZE;

    if (pageNumber.isPresent() && !StringUtils.isEmpty(pageNumber.get())) {
      if (pageNumber.get() >= 1 && pageNumber.get() <= Constants.MAX_PAGE_COUNT) {
        tmpPageNumber = pageNumber.get();
      } else {
        logger.error("pageNumber value must be 1-1000.  Value passed = "
            + pageNumber.get().toString());
      }
    }

    if (pageSize.isPresent() && pageSize.get() > 0) {
      tmpPageSize = pageSize.get();

      if (tmpPageSize < 1) {
        tmpPageSize = Constants.DEFAULT_PAGE_SIZE;
      } else if (tmpPageSize > Constants.MAX_PAGE_SIZE) {
        tmpPageSize = Constants.MAX_PAGE_SIZE;
      }
    }

    String tmpQuery = "";
    if (query.isPresent()) {
      if (query.get() != null && !query.get().isEmpty()) {
        tmpQuery = query
            .get()
            .trim()
            .toLowerCase()
            .replace("'", "''");
      }
    }

    return actorsDao.getActors(tmpPageNumber, tmpPageSize, tmpQuery);
  }
}
package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * ActorController.
 **/
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController {
  @Autowired ActorsDao actorsDao;
  @Autowired ParameterValidator validator;

  private static final Logger logger = LoggerFactory.getLogger(ActorsController.class);


  /** getActor. */
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(
      value = "Get single actor",
      notes = "Retrieve and return a single actor by actor ID")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "The actor object"),
        @ApiResponse(code = 404, message = "An actor with the specified ID was not found")
      })
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
      logger.error("Invalid actorId parameter " + actorId);
      return Mono.justOrEmpty(
          ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).build());
    }
  }

  /** getAllActors. */
  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all actors", notes = "Retrieve and return all actors")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List of actor objects")})
  public Object getAllActors(
      @ApiParam(value = "(query) (optional) The term used to search Actor name") @RequestParam("q")
          final Optional<String> query,
      @ApiParam(value = "1 based page index", defaultValue = "1") @RequestParam
          Optional<String> pageNumber,
      @ApiParam(value = "page size (1000 max)", defaultValue = "100") @RequestParam
          Optional<String> pageSize) {

    String q = null;

    if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
      if (query.get() != null && !query.get().isEmpty()) {
        if (validator.isValidSearchQuery(query.get())) {
          q = query.get().trim().toLowerCase().replace("'", "''");
        } else {
          logger.error("Invalid q(search) parameter");
          MultiValueMap<String, String> headers = new HttpHeaders();
          headers.put("content-type", Arrays.asList("text/plain"));
          return new ResponseEntity<>(
              "Invalid q(search) parameter", headers, HttpStatus.BAD_REQUEST);
        }
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent() && !StringUtils.isEmpty(pageNumber.get())) {
      if (!validator.isValidPageNumber(pageNumber.get())) {
        logger.error("Invalid pageNumber parameter");
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("content-type", Arrays.asList("text/plain"));
        return new ResponseEntity<>(
            "Invalid pageNumber parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent() && !StringUtils.isEmpty(pageSize.get())) {
      if (!validator.isValidPageSize(pageSize.get())) {
        logger.error("Invalid pageSize parameter");
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("content-type", Arrays.asList("text/plain"));
        return new ResponseEntity<>("Invalid pageSize parameter", headers,
            HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    try {
      pageNo--;
      if (pageNo < 0) {
        pageNo = 0;
      }
      return actorsDao.getActors(q, pageNo * pageSz, pageSz);
    } catch (Exception ex) {
      return new ResponseEntity<>("ActorsControllerException",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

package com.microsoft.cse.helium.app.controllers;

import java.util.List;
import java.util.Optional;

import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.models.ActorsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponse;

/**
 * ActorController
 */
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController {

    @Autowired
    private ActorsRepository actorRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all actors", notes = "Retrieve and return all actors")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List of actor objects") })    
    public Mono<Actor> findByActorId(ServerHttpResponse response) {
        return actorRepository.findByActorId("nm0000502");
    }


    // @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    // @ApiOperation(value = "Get single actor", notes = "Retrieve and return a single actor by actor ID")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 200, message = "The actor object"),
    //         @ApiResponse(code = 404, message = "An actor with the specified ID was not found") })
    // public ResponseEntity<Actor> getActor(@ApiParam(value = "The ID of the actor to look for", example = "nm0000002", required = true) @PathVariable("id")  String actorId) {
    //     Mono<Actor> actor = actorRepository.findByActorId(actorId);
    //     if (actor.isPresent()) {
    //         return new ResponseEntity<>(actor.get(), HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }

}
package com.microsoft.cse.helium.app.controllers;

import java.util.Optional;
import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.models.ActorsRepository;
import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponse;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;

/**
 * ActorController
 */
@RestController
@RequestMapping(path = "/api/actors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Actors")
public class ActorsController {
    @Autowired
    ActorsDao actorsDao;

    @Autowired
    private ActorsRepository actorRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get single actor", notes = "Retrieve and return a single actor by actor ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The actor object"),
            @ApiResponse(code = 404, message = "An actor with the specified ID was not found") })
    public Mono<Actor> getActor(
            @ApiParam(value = "The ID of the actor to look for", example = "nm0000002", required = true) @PathVariable("id") String actorId) {
        return actorRepository.findByActorId(actorId);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all actors", notes = "Retrieve and return all actors")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List of actor objects") })
    public Flux<Actor> getAllActors(
            @ApiParam(value = "(query) (optional) The term used to search Actor name", required = false) @RequestParam final Optional<String> q,
            @RequestParam("q") final Optional<String> query,
            @ApiParam(value = "0 based page index", defaultValue = "0") @RequestParam Optional<Integer> pageNumber,
            @ApiParam(value = "page size (1000 max)", defaultValue = "100") @RequestParam Optional<Integer> pageSize,
            ServerHttpResponse response) {
        Integer _pageNumber = 0;
        Integer _pageSize = Constants.DefaultPageSize;

        if (pageNumber.isPresent() && !StringUtils.isEmpty(pageNumber.get())) {
            if (pageNumber.get() >= 1) {
                _pageNumber = pageNumber.get();
            }
        }

        if (pageSize.isPresent() && pageSize.get() > 0) {
            _pageSize = pageSize.get();

            if (_pageSize < 1) {
                _pageSize = Constants.DefaultPageSize;
            } else if (_pageSize > Constants.MaxPageSize) {
                _pageSize = Constants.MaxPageSize;
            }
        }

        String _q = "";

        if(query.isPresent()) {
            if(query.get() != null && !query.get().isEmpty()) {
                String tmpQ = query.get().trim().toLowerCase().replace("'", "''");
                _q = " and contains(m.textSearch, '" + tmpQ + "') ";
            }
        }

        return actorsDao.getActors(_pageNumber, _pageSize, _q);
    }
}
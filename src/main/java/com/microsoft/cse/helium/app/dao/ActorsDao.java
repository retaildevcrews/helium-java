package com.microsoft.cse.helium.app.dao;

import com.microsoft.cse.helium.app.services.configuration.*;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.models.Actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorsDao extends BaseCosmosDbDao {

    private static final Logger _logger = LoggerFactory.getLogger(ActorsDao.class);
    final String _actorSelect = "select m.id, m.partitionKey, m.actorId, m.type, m.name, m.birthYear, m.deathYear, m.profession, m.textSearch, m.movies from m where m.type = 'Actor' ";
    final String _actorContains = " and contains(m.textSearch, \"%s\") ";
    final String _actorOrderBy = " order by m.name ";
    final String _actorOffset = " offset %d limit %d ";

    final String _actorSelectById = _actorSelect + " and m.actorId = '%s'";

    public ActorsDao(IConfigurationService configService){
        super(configService);
    }
    public Mono<Actor> getActorById(String _actorId)  {
        final String query = String.format(_actorSelectById, _actorId.toString());

        ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();
        Mono<Actor> actor =
                this._context
                .getBean(CosmosClient.class)
                .getDatabase(this._cosmosDatabase)
                .getContainer(this._cosmosContainer)
                .queryItems(query, this._feedOptions)
                .flatMap(cosmosItemFeedResponse -> Mono.justOrEmpty(cosmosItemFeedResponse
                        .results()
                        .stream()
                        .map(cosmosItem -> cosmosItem.toObject(Actor.class))
                        .findFirst()))
                .onErrorResume(Mono::error)
                .next();
        return actor;

    }

    public Flux<Actor> getActors(Integer _pageNumber, Integer _pageSize, String _query) {
        ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

        String _contains = "";
        if(!_query.isEmpty()) {
            _contains = String.format(_actorContains, _query);
        }

        String queryString = _actorSelect + _contains +  _actorOrderBy + String.format(_actorOffset, _pageNumber, _pageSize);

        Flux<FeedResponse<CosmosItemProperties>> feedResponse = this._context.getBean(CosmosClient.class).getDatabase(this._cosmosDatabase)
                .getContainer(this._cosmosContainer).queryItems(queryString, this._feedOptions);

        Flux<Actor> selectedActors = feedResponse.flatMap(flatFeedResponse -> {
            return Flux.fromIterable(flatFeedResponse.results());
        }).flatMap(cosmosItemProperties -> {

            try {
                return Flux.just(objMapper.readValue(cosmosItemProperties.toJson(), Actor.class));
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Flux.empty();
        });

        return selectedActors;
    }
}
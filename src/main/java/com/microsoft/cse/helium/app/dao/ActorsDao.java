package com.microsoft.cse.helium.app.dao;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosItemProperties;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.cosmosdb.core.convert.ObjectMapperFactory;
import com.microsoft.cse.helium.app.models.Actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorsDao {

    @Autowired
    ApplicationContext _context;

    final String _actorSelect = "select m.id, m.partitionKey, m.actorId, m.type, m.name, m.birthYear, m.deathYear, m.profession, m.textSearch, m.movies from m where m.type = 'Actor' ";
    final String _actorContains = " and contains(m.textSearch, \"%s\") ";
    final String _actorOrderBy = " order by m.name ";
    final String _actorOffset = " offset %d limit %d ";

    public Flux<Actor> getActors(Integer _pageNumber, Integer _pageSize, String _query) {

        final FeedOptions options = new FeedOptions();
        options.enableCrossPartitionQuery(true);
        options.maxDegreeOfParallelism(2);

        ObjectMapper objMapper = ObjectMapperFactory.getObjectMapper();

        String _contains = "";
        if(!_query.isEmpty()) {
            _contains = String.format(_actorContains, _query);
        }

        String queryString = _actorSelect + _contains +  _actorOrderBy + String.format(_actorOffset, _pageNumber, _pageSize);

        Flux<FeedResponse<CosmosItemProperties>> feedResponse = _context.getBean(CosmosClient.class).getDatabase("imdb")
                .getContainer("actors").queryItems(queryString, options);

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
package com.microsoft.cse.helium.app.models;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.microsoft.azure.spring.data.cosmosdb.repository.ReactiveCosmosRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * ActorsRepository
 */
@Repository
public interface ActorsRepository extends ReactiveCosmosRepository<Actor, String> {
    Mono<Actor> findByActorId(String actorId);

    Flux<Actor> findByTextSearchContainingOrderByActorId(String nameString);

}
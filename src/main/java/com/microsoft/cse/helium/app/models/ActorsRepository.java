package com.microsoft.cse.helium.app.models;

import com.microsoft.azure.spring.data.cosmosdb.repository.ReactiveCosmosRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;


/**
 * ActorsRepository
 */
@Repository
public interface ActorsRepository extends ReactiveCosmosRepository<Actor, String> {
    Flux<Actor> findByActorId(String actorId);
   //Flux<Actor> findByTextSearchContainingOrderByActorId(String actorName, Pageable pageable);
}
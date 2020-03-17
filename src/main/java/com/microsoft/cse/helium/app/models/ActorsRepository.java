package com.microsoft.cse.helium.app.models;

import com.microsoft.azure.spring.data.cosmosdb.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * ActorsRepository.
 */
@Repository
public interface ActorsRepository extends ReactiveCosmosRepository<Actor, String> {
  Mono<Actor> findByActorId(String actorId);
}
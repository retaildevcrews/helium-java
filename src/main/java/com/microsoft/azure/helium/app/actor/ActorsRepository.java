package com.microsoft.azure.helium.app.actor;

import java.util.List;

//import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;
import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorsRepository extends CosmosRepository<Actor, String> {
     List<Actor> findByActorId(String actorId);
     Page<Actor> findByTextSearchContainingOrderByActorId(String actorName, Pageable pageable);
}
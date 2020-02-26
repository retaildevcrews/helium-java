package com.microsoft.cse.helium.app.services.Actors;

import com.microsoft.cse.helium.app.models.Actor;
import com.microsoft.cse.helium.app.models.ActorsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ActorsService {

    @Autowired
    private ActorsRepository repository;

    // Flux<Actor> findByActorId(String actorId) {
    //     return Flux.just(new Actor());
    // }
    
    // Flux<Actor> findByTextSearchContainingOrderByActorId(String actorName, Pageable pageable){
    //     return Flux.just(new Actor());
    // }
}
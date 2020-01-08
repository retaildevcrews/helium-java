package com.microsoft.azure.helium.app.actor;

import java.util.List;
import java.util.Optional;

import com.microsoft.azure.spring.data.cosmosdb.core.query.DocumentDbPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * ActorsService
 */
@Service
public class ActorsService {

    @Autowired
    private ActorsRepository repository;

  /*  public List<Actor> getAllActors(Optional<String> query, Integer pageNo, Integer pageSize, Sort sort) {
        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            return repository.findByTextSearchContainingOrderByActorId(query.get().toLowerCase());
        } else {
            return (List<Actor>) repository.findAll(sort);
        }
    }*/

    public List<Actor> getAllActors(Optional<String> query, Integer pageNo, Integer pageSize, Sort sort) {
        final Pageable pageable = new DocumentDbPageRequest(pageNo, pageSize, null, sort);
        List<Actor> content = null;
        Page<Actor> page = null;
        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            page = repository.findByTextSearchContainingOrderByActorId(query.get().toLowerCase(), page.getPageable());
        } else {
            page = repository.findAll(pageable);
        }

        if (pageNo == 0) {
            content = page.getContent();
            for (Actor actor : content) System.out.println(actor.toString());
            return content;
        } else {
            Page<Actor> nextPage = null;
            for (int i = 1; i <= pageNo; i++) {
                nextPage = this.repository.findAll(page.getPageable());
                // reset page to nextpage like a linkedlist
                page = nextPage;
                content = nextPage.getContent();
                for (Actor actor : content) System.out.println(actor.toString());
            }
            return content;
        }
    }


    public Optional<Actor> getActor(String actorId) {
        if (StringUtils.isEmpty(actorId)) {
            throw new NullPointerException("actorId cannot be empty or null");
        }

        List<Actor> actors = repository.findByActorId(actorId);
        if (actors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(actors.get(0));
        }
    }
}
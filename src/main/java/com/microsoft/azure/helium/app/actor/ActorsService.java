package com.microsoft.azure.helium.app.actor;

import java.util.List;
import java.util.Optional;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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



    public List<Actor> getAllActors(Optional<String> query, Integer pageNo, Integer pageSize, Sort sort) {

        List<Actor> content = null;
        Page<Actor> page = null;
        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            System.out.println("query is " +query.get().toLowerCase() + "pageNo " + pageNo + "pageSize " +pageSize );
            final Pageable pageable = new CosmosPageRequest(pageNo, pageSize, null);
            page = repository.findByTextSearchContainingOrderByActorId(query.get().toLowerCase(), pageable);
        } else {
            final Pageable pageable = new CosmosPageRequest(pageNo, pageSize, null,sort);

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
package com.microsoft.azure.helium.app.actor;

import java.util.List;
import java.util.Optional;

import com.microsoft.azure.spring.data.cosmosdb.Constants;
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



    public List<Actor> getAllActors(Optional<String> query, Optional<Integer> pageNo, Optional<Integer> pageSize, Sort sort) {

        List<Actor> content = null;
        Page<Actor> page = null;
        Integer pageNumber = 0;
        Integer pages = 0;


        if(pageNo.isPresent() && !StringUtils.isEmpty(pageNo.get())) {
            System.out.println("pageNo is " +pageNo.get() + "pageNo " + pageNo + "pageSize " +pageSize );
            pageNumber = pageNo.get();
        }
        if(pageSize.isPresent() && pageSize.get() > 0) {
            System.out.println("pageNo is " + pageNo.get() + "pageNo " + pageNo + "pageSize " + pageSize);
            pages = pageSize.get();
        }
        if (pages < 1) {
            pages = com.microsoft.azure.helium.app.Constants.DefaultPageSize;
        } else if (pages > com.microsoft.azure.helium.app.Constants.MaxPageSize) {
            pages = com.microsoft.azure.helium.app.Constants.MaxPageSize;
        }


        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            System.out.println("query is " +query.get().toLowerCase() + "pageNo " + pageNo + "pageSize " +pages );
            final Pageable pageable = new CosmosPageRequest(pageNumber, pages, null);
            page = repository.findByTextSearchContainingOrderByActorId(query.get().toLowerCase(), pageable);
        } else {
            System.out.println("pageNo " + pageNo + "pageSize " +pages );

            final Pageable pageable = new CosmosPageRequest(pageNumber, pages, null,sort);

            page = repository.findAll(pageable);
        }

        if (pageNumber == 0) {
            content = page.getContent();
            for (Actor actor : content) System.out.println(actor.toString());
            return content;
        } else {
            Page<Actor> nextPage = null;
            for (int i = 1; i <= pageNumber; i++) {
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
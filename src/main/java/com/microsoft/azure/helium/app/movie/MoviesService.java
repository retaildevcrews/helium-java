package com.microsoft.azure.helium.app.movie;

import com.google.gson.Gson;
import com.microsoft.azure.spring.data.cosmosdb.core.query.DocumentDbPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;


/**
 * MoviesService
 */
@Service
public class MoviesService {

    @Autowired
    MoviesRepository repository;

    private static Gson gson = new Gson();



    public List<Movie> getAllMovies(Optional<String> query, Integer pageNo, Integer pageSize, Sort sort) {
        final Pageable pageable = new DocumentDbPageRequest(pageNo, pageSize, null, sort);
        List<Movie> content = null;
        Page<Movie> page = null;
        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            page = repository.findByTextSearchContaining(query.get().toLowerCase(), page.getPageable());
        } else {
            page = repository.findAll(pageable);
        }

        if (pageNo == 0) {
            content = page.getContent();
            for (Movie movie : content) System.out.println(movie.toString());
            return content;
        } else {
            Page<Movie> nextPage = null;
            for (int i = 1; i <= pageNo; i++) {
                nextPage = this.repository.findAll(page.getPageable());
                content = page.getContent();
                /* reset page to nextpage like a linkedlist*/
                page = nextPage;
                content = nextPage.getContent();
                for (Movie movie : content) System.out.println(movie.toString());
            }

            return content;
        }
    }


    /*This API uses QueryDocument with PartitionKey */
    public Optional<Movie> getMovie(String movieId) {
        if (StringUtils.isEmpty(movieId)) {
            throw new NullPointerException("movieId cannot be empty or null");
        }
        //queries by partitionid - partitionkey is the field annotated with @partitionkey
        List<Movie> movies = repository.findByMovieId(movieId);
        //queries without partitionkey
        //repository.findById(movieId);
        if (movies.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(movies.get(0));
        }
    }

}




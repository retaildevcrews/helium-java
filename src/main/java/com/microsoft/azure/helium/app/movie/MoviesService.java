package com.microsoft.azure.helium.app.movie;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<Movie> getAllMovies(Optional<String> query, Sort sort) {
        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            return repository.findByTextSearchContaining(query.get().toLowerCase());
        } else {
            return (List<Movie>) repository.findAll(sort);
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




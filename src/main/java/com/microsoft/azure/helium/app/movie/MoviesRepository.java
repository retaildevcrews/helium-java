package com.microsoft.azure.helium.app.movie;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;

/**
 * MoviesRepository
 */
@Repository
public interface MoviesRepository extends DocumentDbRepository<Movie, String>  {
    List<Movie> findByMovieId(String movieId);
    List<Movie> findByTextSearchContaining(String movieName);

}
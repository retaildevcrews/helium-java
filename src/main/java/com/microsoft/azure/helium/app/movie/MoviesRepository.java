package com.microsoft.azure.helium.app.movie;
import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import retrofit2.http.Query;

import java.util.List;



/**
 * MoviesRepository
 */
@Repository
public interface MoviesRepository extends CosmosRepository<Movie, String> {
    List<Movie> findByMovieId(String movieId);


}
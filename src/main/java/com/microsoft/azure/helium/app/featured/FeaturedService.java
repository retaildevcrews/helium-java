package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.helium.app.movie.Movie;
import com.microsoft.azure.helium.app.movie.MoviesRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * ActorsService
 */
@Service
public class FeaturedService {

    @Autowired
    private FeaturedRepository repository;

    @Autowired
    private MoviesRepository moviesRepository;

    Random _rand = new Random(DateTime.now().getMillis());


    public List<Featured> getAllFeaturedMovies() {
            return (List<Featured>) repository.findAll();
    }


    public Movie getFeaturedMovie(){
        List<Featured> featuredMovies = getAllFeaturedMovies();
        List<String> list = new ArrayList<>();
        int movieCount = featuredMovies.size();

        if(featuredMovies.size() > 0){
        for (Featured f : featuredMovies) {
            // apply weighting
            for (int i = 0; i < f.getWeight(); i++) {
                list.add(f.getMovieId());
            }
        }
        }else{
            // default to The Matrix
            list.add("tt0133093");
        }

        // get random featured movie by movieId
        // CosmosDB API will throw an exception on a bad movieId
        int randMovieIdIdx = getRandomNumberInRange(0, movieCount-1);
        System.out.println("randMovieIdIdx "+ randMovieIdIdx);
        String featuredMovieId = featuredMovies.get(randMovieIdIdx).getMovieId();
        System.out.println("featuredMovieId "+ featuredMovieId);

        List<Movie> movies = moviesRepository.findByMovieId(featuredMovieId);
        System.out.println("movieId "+ movies.get(0).getMovieId());


        return movies.get(0);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
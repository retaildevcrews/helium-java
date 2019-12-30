package com.microsoft.azure.helium.app.genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.microsoft.azure.helium.app.movie.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * GenresService
 */
@Service
public class GenresService {

    @Autowired
    private GenresRepository repository;

    public List<String> getAllGenres() {
        Iterable<Genre> genres = repository.findAll();
        Stream<Genre> genresStream = StreamSupport.stream(genres.spliterator(), false);
        return genresStream.map(g -> g.getGenre()).collect(Collectors.toList());
    }
}
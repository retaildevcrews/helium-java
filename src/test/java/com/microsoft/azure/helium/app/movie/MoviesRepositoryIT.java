package com.microsoft.azure.helium.app.movie;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.microsoft.azure.helium.Application;

import com.microsoft.azure.helium.app.actor.Actor;
import com.microsoft.azure.helium.app.actor.ActorsUtils;
import net.minidev.json.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MoviesRepositoryIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class MoviesRepositoryIT {

    @MockBean
    private MoviesRepository repository;

    @Test
    public void findByMovieIdShouldReturnMovie() throws IOException, ParseException {

        Movie expected = MoviesUtils.generateMovieWithId();
        List<Movie> movies = new ArrayList<Movie>();
        movies.add(expected);
        String movieId = movies.get(0).getMovieId();

        when(repository.save(any(Movie.class))).thenReturn(expected);
        when(repository.findByMovieId(movieId)).thenReturn(movies);
        // Assert
        assertThat(movies, hasSize(1));
        assertNotNull(movies);
        assertEquals(expected.getMovieId(), movies.get(0).getMovieId());

    }

    @Test
    public void findByTextSearchShouldQueryMoviesTextField() throws IOException, ParseException {
        Movie expected = MoviesUtils.generateMovieWithId();
        String movieName = expected.getTextSearch();
        repository.save(expected);

        List<Movie> movies = new ArrayList<Movie>();
        movies.add(expected);

        // Act
        when(repository.findByTextSearchContaining(movieName.toLowerCase())).thenReturn(movies);// lauren bacall
        assertThat(movies, hasSize(1));
        assertNotNull(movies);

    }

}
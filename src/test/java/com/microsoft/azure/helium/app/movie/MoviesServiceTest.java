package com.microsoft.azure.helium.app.movie;

import static com.microsoft.azure.helium.app.movie.MoviesUtils.expectedMovieResponse;
import static com.microsoft.azure.helium.app.movie.MoviesUtils.generateMovies;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * MoviesServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
public class MoviesServiceTest {

    @Mock
    private MoviesRepository repository;

    @InjectMocks
    private MoviesService service;


    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingMovieWithNullMovieId() {
        String movieId = null;

        service.getMovie(movieId);
    }

   @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingMovieWithEmptyMovieId() {
        String movieId = "";

        service.getMovie(movieId);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNotFindingMovie() throws Exception {
        // Arrange
        List<Movie> expected = new ArrayList<>();
        when(repository.findByMovieId(anyString())).thenReturn(expected);

        // Act
        Optional<Movie> actual = service.getMovie(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByMovieId(anyString());
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    public void shouldReturnMovieInOptionalWhenFindingMovie() throws Exception {
        // Arrange
        Movie expected = mock(Movie.class);
        List<Movie> list = Arrays.asList(expected);
        when(repository.findByMovieId(anyString())).thenReturn(list);

        // Act
        Optional<Movie> actual = service.getMovie(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByMovieId(anyString());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
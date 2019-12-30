package com.microsoft.azure.helium.app.genre;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.microsoft.azure.helium.app.genre.GenresService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * GenresServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
public class GenresServiceTest {

    @Mock
    private GenresRepository repository;

    @InjectMocks
    private GenresService service;

    @Test
    public void genresServiceShouldReturnListofAllGenres() throws Exception {
        // Arrange
        List<String> expected = Arrays.asList("Action",
                "Adventure",
                "Animation",
                "Biography",
                "Comedy",
                "Crime",
                "Documentary",
                "Drama",
                "Family",
                "Fantasy",
                "History",
                "Music",
                "Musical",
                "Mystery",
                "Romance",
                "Sci-Fi",
                "Sport",
                "Thriller",
                "War");
        Iterable<Genre> genres = GenresUtils.generateGenres();
        when(repository.findAll()).thenReturn(genres);

        // Act
        List<String> genresStr = service.getAllGenres();

        // Assert
        assertNotNull(genresStr);
        assertThat(genresStr, hasSize(expected.size()));
        assertThat(genresStr, containsInAnyOrder(expected.toArray()));
    }

}
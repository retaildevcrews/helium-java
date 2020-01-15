package com.microsoft.azure.helium.app.movie;
import static com.microsoft.azure.helium.app.movie.MoviesUtils.expectedMovieResponse;
import static com.microsoft.azure.helium.app.movie.MoviesUtils.generateMovies;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
/**
 * MoviesControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {
    
     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoviesService service;

   @Test
    public void getMoviesEndpointShouldReturnAllMoviesFromService() throws Exception {

        List<Movie> mockMovies = generateMovies();
        when(service.getAllMovies(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(mockMovies);
        String expected = expectedMovieResponse();

        MvcResult result = this.mockMvc
                .perform(get("/api/movies").param("pageNumber", "1").param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)))
                .andDo(print())
                .andReturn();

    }

    @Test
    public void getSingleMovieEndpointShouldReturnValidMovieFromService() throws Exception {
        List<Movie> mockMovies = generateMovies();
        String id = mockMovies.get(0).getMovieId();
        Movie expected =  mockMovies.get(0);
        when(service.getMovie(any())).thenReturn(Optional.of(expected));

        ResultActions result = this.mockMvc
                .perform(get("/api/movies/{movieId}", id))
                .andDo(print());

        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).getMovie(any());
    }

    @Test
    public void getMoviesEndpointShouldReturnNotFoundMovieFromService() throws Exception {
        // Arrange
        when(service.getMovie(any())).thenReturn(Optional.empty());

        // Act
        ResultActions result = this.mockMvc
                .perform(get("/api/movies/{movieId}", UUID.randomUUID().toString()))
                .andDo(print());

        // Assert
        result.andExpect(status().isNotFound());
        verify(service, times(1)).getMovie(any());
    }

}
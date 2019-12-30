package com.microsoft.azure.helium.app.genre;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import com.microsoft.azure.helium.app.genre.GenresController;
import com.microsoft.azure.helium.app.genre.GenresService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * GenresControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GenresController.class)
public class GenresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenresService service;

    @Test
    public void genresEndpointShouldReturnAllGenresFromService() throws Exception {
        // Arrange
        List<String> genres = Arrays.asList("Animation", "Comedy", "Sci-Fi");
        when(service.getAllGenres()).thenReturn(genres);

        // Act
        ResultActions action = this.mockMvc
                .perform(get("/api/genres/"))
                .andDo(print());

        // Assert
        action
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("Animation")))
                .andExpect(jsonPath("$[1]", is("Comedy")))
                .andExpect(jsonPath("$[2]", is("Sci-Fi")));
    }

}

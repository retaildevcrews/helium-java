package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.helium.app.movie.MoviesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * FeaturedControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FeaturedController.class)
public class FeaturedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeaturedService service;
    @Test
    public void getFeaturedMovieEndpointShouldReturnAllActorsFromService() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get("/api/featured/movie"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }


}
package com.microsoft.azure.helium.health;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CosmosDbHealthIndicatorIT
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CosmosDbHealthIndicatorIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void healthCheckEndpointShouldReturnSuccess() throws Exception {
        this.mockMvc
            .perform(get("/healthz"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")))
            .andExpect(jsonPath("$.details.cosmosDb.status", is("UP")))
            .andExpect(jsonPath("$.details.cosmosDb.details", is("\"Movies\":100,\"Actors\":531,\"Genres\":19,\"Version\":1L}")));

        this.mockMvc
            .perform(get("/healthz/cosmosDb"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")));
    }
}
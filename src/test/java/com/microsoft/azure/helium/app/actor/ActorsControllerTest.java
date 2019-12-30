package com.microsoft.azure.helium.app.actor;

import static com.microsoft.azure.helium.app.actor.ActorsUtils.expectedActorResponse;
import static com.microsoft.azure.helium.app.actor.ActorsUtils.generateActors;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
 * ActorsControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ActorsController.class)
public class ActorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorsService service;

    @Test
    public void getActorsEndpointShouldReturnAllActorsFromService() throws Exception {

        List<Actor> mockActors = generateActors();
        when(service.getAllActors(any(), any())).thenReturn(mockActors);
        String expected = expectedActorResponse();
        MvcResult result = this.mockMvc
                .perform(get("/api/actors"))
                .andDo(print())
                .andReturn();

        String actual = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expected, actual, false);

    }

    @Test
    public void getSingleActorEndpointShouldReturnValidActorFromService() throws Exception {
        List<Actor> mockActors = generateActors();
        String id = mockActors.get(0).getActorId();
        Actor expected =  mockActors.get(0);
        when(service.getActor(any())).thenReturn(Optional.of(expected));

        ResultActions result = this.mockMvc
                .perform(get("/api/actors/{actorId}", id))
                .andDo(print());

        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).getActor(any());
    }

   @Test
    public void getActorsEndpointShouldReturnNotFoundActorFromService() throws Exception {
        // Arrange
        when(service.getActor(any())).thenReturn(Optional.empty());

        // Act
        ResultActions result = this.mockMvc
                .perform(get("/api/actors/{actorId}", UUID.randomUUID().toString()))
                .andDo(print());

        // Assert
        result.andExpect(status().isNotFound());
        verify(service, times(1)).getActor(any());
    }
}
package com.microsoft.azure.helium.app.actor;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.*;

import com.microsoft.azure.helium.Application;
import net.minidev.json.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ActorsRepositoryIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class ActorsRepositoryIT {

    @MockBean
    private ActorsRepository repository;

    @Test
    public void findByActorIdShouldReturnActor() throws IOException, ParseException {
        // Arrange
        Actor expected = ActorsUtils.generateActorWithId();
        List<Actor> actors = new ArrayList<Actor>();
        actors.add(expected);
        String actorId = actors.get(0).getActorId();

        when(repository.save(any(Actor.class))).thenReturn(expected);
        when(repository.findByActorId(actorId)).thenReturn(actors);
        // Assert
        assertThat(actors, hasSize(1));
        assertNotNull(actors);
        assertEquals(expected.getActorId(), actors.get(0).getActorId());



    }


    @Test
    public void findByTextSearchShouldQueryActorsTextField() throws IOException, ParseException {
        // Arrange
        //lauren bacall
        //nm0000002
        Actor expected = ActorsUtils.generateActorWithId();
        String actorName = expected.getName();
        repository.save(expected);

        List<Actor> actors = new ArrayList<Actor>();
        actors.add(expected);

        // Act
        when(repository.findByTextSearchContainingOrderByActorId(actorName.toLowerCase())).thenReturn(actors);// lauren bacall
        assertThat(actors, hasSize(1));
        assertNotNull(actors);
        assertEquals(expected.getName(), actors.get(0).getName());

    }

}
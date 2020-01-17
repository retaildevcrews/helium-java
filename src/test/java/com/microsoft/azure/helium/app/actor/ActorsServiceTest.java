package com.microsoft.azure.helium.app.actor;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * ActorsServiceTest
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ActorsServiceTest {

    @Mock
    private ActorsRepository repository;

    @InjectMocks
    private ActorsService service;


    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingActorWithNullActorId() {
        String actorId = null;
        service.getActor(actorId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingActorWithEmptyActorId() {
        String actorId = "";
        service.getActor(actorId);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNotFindingActor() throws Exception {
        // Arrange
        List<Actor> expected = new ArrayList<>();
        when(repository.findByActorId(anyString())).thenReturn(expected);

        // Act
        Optional<Actor> actual = service.getActor(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByActorId(anyString());
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    public void shouldReturnActorInOptionalWhenFindingActor() throws Exception {
        // Arrange
        Actor expected = mock(Actor.class);
        List<Actor> list = Arrays.asList(expected);
        when(repository.findByActorId(anyString())).thenReturn(list);

        // Act
        Optional<Actor> actual = service.getActor(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByActorId(anyString());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
package com.microsoft.azure.helium.health;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import com.microsoft.azure.helium.app.movie.Movie;
import com.microsoft.azure.helium.config.BuildConfig;
import com.microsoft.azure.helium.utils.IntegrationTestsUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CosmosDbHealthIndicatorTest
 */
@RunWith(MockitoJUnitRunner.class)

public class CosmosDbHealthIndicatorTest {

    @Spy
    CosmosDbHealthIndicator indicator = new CosmosDbHealthIndicator("database");

    @Test
    public void healthCheckIndicatorShouldReturnUp() throws Exception {
        // Arrange
        Builder builder = new Builder();

        Mockito.doReturn(getSuccessResponse()).when(indicator).getStatusCode(anyString());

        // Act
        indicator.doHealthCheck(builder);
        Health health = builder.build();

        // Assert
        assertNotNull(health);
        assertThat(health.getStatus(), is(Status.UP));
    }

    @Test
    public void healthCheckIndicatorShouldReturnDown() throws Exception {
        // Arrange
        Builder builder = new Builder();
        Mockito.doReturn(getFailureResponse()).when(indicator).getStatusCode(anyString());

        // Act
        indicator.doHealthCheck(builder);
        Health health = builder.build();

        // Assert
        assertNotNull(health);
        assertThat(health.getStatus(), is(Status.DOWN));
    }

    private HashMap<String, Integer> getSuccessResponse(){

        Integer statusCode = IntegrationTestsUtils.getRandomBetween(200, 204);
        HashMap<String, Integer> resultDetails = new HashMap<>();
        resultDetails.put("status", statusCode);
        resultDetails.put("actors", 4434);
        resultDetails.put("movies", 1303);
        resultDetails.put("genres", 21);
        resultDetails.put("version",1);
        return resultDetails;
    }

    private HashMap<String, Long> getFailureResponse(){
        Long statusCode = (long) (int) IntegrationTestsUtils.getRandomBetween(400, 600);
        HashMap<String, Long> resultDetails = new HashMap<>();
        resultDetails.put("status", statusCode);
        return resultDetails;
    }

}
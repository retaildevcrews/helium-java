package com.microsoft.cse.helium.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "20000")
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:application.properties")
@SpringBootTest

public class MovieControllerTest {
  @Autowired
  private WebTestClient webClient;


    @Test
    public void testGetMovieByIdForValidMovie() {
        String validMovieId = "tt0120737";
        webClient.get().uri("/api/movies/{id}", validMovieId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(validMovieId);
    }
    @Test
    public void testGetMovieByIdForMovieBadRequest() {
        webClient.get()
                .uri("/api/movies/{id}", "nm0000002")
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    public void testGetMovieByIdForMovieNotFound() {
        webClient.get()
                .uri("/api/movies/{id}", "tt111111")
                .exchange()
                .expectStatus().isNotFound();
    }
}



package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.models.Movie;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

  public void testBadQueryMovies_1(){
    webClient.get().uri("/api/movies?q=a")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid q (search) parameter");
  }
  @Test
  public void testBadQueryMovies_2(){
    webClient.get().uri("/api/movies?q=123456789012345678901")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid q (search) parameter");
  }
  @Test
  public void testBadPageSize_1(){
    webClient.get().uri("/api/movies?pageSize=0")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
  }
  @Test
  public void testBadPageSize_2(){
    webClient.get().uri("/api/movies?pageSize=-1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
  }
  @Test
  public void testBadPageSize_3(){
    webClient.get().uri("/api/movies?pageSize=1001")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
  }
  @Test
  public void testBadPageSize_4(){
    webClient.get().uri("/api/movies?pageSize=foo")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
  }
  @Test
  public void testBadPageSize_5(){
    webClient.get().uri("/api/movies?pageSize=10.1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
  }
  @Test
  public void testBadPageNumber_1(){
    webClient.get().uri("/api/movies?pageNumber=0")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
  }
  @Test
  public void testBadPageNumber_2(){
    webClient.get().uri("/api/movies?pageNumber=-1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
  }

  @Test
  public void testBadPageNumber_3(){
    webClient.get().uri("/api/movies?pageNumber=10001")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
  }

  @Test
  public void testBadPageNumber_4(){
    webClient.get().uri("/api/movies?pageNumber=foo")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
  }
  @Test
  public void testBadPageNumber_5(){
    webClient.get().uri("/api/movies?pageNumber=10.1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
  }

  @Test
  public void testAllMovies_1() {
    webClient.get().uri("/api/movies")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk()
        .expectBodyList(Movie.class);
  }

  @Test
  public void testMoviesWithValidYear_1() {
    webClient.get().uri("/api/movies?year=2005")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk()
        .expectBodyList(Movie.class).consumeWith(movies ->
        Assert.assertEquals(movies.getResponseBody().get(0).getYear(), 2005));
  }

  @Test
  public void testMoviesWithValidYear_2(){
    webClient.get().uri("/api/movies?year=2025")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectBodyList(Movie.class).hasSize(0);
  }

  @Test
  public void testMoviesWithValidYear_3(){
    webClient.get().uri("/api/movies?year=1874")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectBodyList(Movie.class).hasSize(0);
  }

  @Test
  public void testMoviesWithBadYear_1(){
    webClient.get().uri("/api/movies?year=foo")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_2(){
    webClient.get().uri("/api/movies?year=-1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_3(){
    webClient.get().uri("/api/movies?year=0")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_4(){
    webClient.get().uri("/api/movies?year=1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_5(){
    webClient.get().uri("/api/movies?year=1873")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_6(){
    webClient.get().uri("/api/movies?year=2026")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }

  @Test
  public void testMoviesWithBadYear_7(){
    webClient.get().uri("/api/movies?year=2020.1")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectHeader().contentType(MediaType.TEXT_PLAIN)
        .expectStatus().isBadRequest()
        .expectBody(String.class).isEqualTo("Invalid Year parameter");
  }


}


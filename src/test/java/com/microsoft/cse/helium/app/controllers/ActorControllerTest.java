package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.models.Actor;
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
@SpringBootTest(properties = {"helium.keyvault.name=${KeyVaultName}", "helium.environment.flag=${AUTH_TYPE}"})

public class ActorControllerTest {
  @Autowired
  private WebTestClient webClient;


    @Test
    public void testGetActorByIdForValidActor() {
        String validActorId = "nm1265067";
        webClient.get().uri("/api/actors/{id}", validActorId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(validActorId);
    }
    @Test
    public void testGetActorByIdForActorBadRequest() {
        webClient.get()
                .uri("/api/actors/{id}", "tt0000002")
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    public void testGetActorByIdForActorNotFound() {
        webClient.get()
                .uri("/api/actors/{id}", "nm111111")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    public void testActors(){
        webClient.get().uri("/api/actors")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Actor.class);
    }
    @Test
    public void testBadQueryActors_1(){
        webClient.get().uri("/api/actors?q=a")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid q (search) parameter");
    }
    @Test
    public void testBadQueryActors_2(){
        webClient.get().uri("/api/actors?q=123456789012345678901")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid q (search) parameter");
    }
    @Test
    public void testBadPageSize_1(){
        webClient.get().uri("/api/actors?pageSize=0")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
    }
    @Test
    public void testBadPageSize_2(){
        webClient.get().uri("/api/actors?pageSize=-1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
    }
    @Test
    public void testBadPageSize_3(){
        webClient.get().uri("/api/actors?pageSize=1001")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
    }
    @Test
    public void testBadPageSize_4(){
        webClient.get().uri("/api/actors?pageSize=foo")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
    }
    @Test
    public void testBadPageSize_5(){
        webClient.get().uri("/api/actors?pageSize=10.1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageSize parameter");
    }
    @Test
    public void testBadPageNumber_1(){
        webClient.get().uri("/api/actors?pageNumber=0")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
    }
    @Test
    public void testBadPageNumber_2(){
        webClient.get().uri("/api/actors?pageNumber=-1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
    }

    @Test
    public void testBadPageNumber_3(){
        webClient.get().uri("/api/actors?pageNumber=10001")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
    }

    @Test
    public void testBadPageNumber_4(){
        webClient.get().uri("/api/actors?pageNumber=foo")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
    }
    @Test
    public void testBadPageNumber_5(){
        webClient.get().uri("/api/actors?pageNumber=10.1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectHeader().contentType(MediaType.TEXT_PLAIN)
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid PageNumber parameter");
    }
}



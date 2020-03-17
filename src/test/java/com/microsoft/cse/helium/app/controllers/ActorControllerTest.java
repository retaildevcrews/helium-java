package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.models.Actor;
import net.minidev.json.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.io.IOException;


@AutoConfigureWebTestClient(timeout = "20000")
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:application.properties")
@SpringBootTest(properties = {"helium.keyvault.name=${KeyVaultName}", "helium.environment.flag=${He_EnvironmentFlag}"})

public class ActorControllerTest {
  @Autowired
  private WebTestClient webClient;

  @Test
  public void testGetActorByIdForValidActor() throws IOException, ParseException {
    final String validActorId = "nm1265067";
    webClient.get().uri("/api/actors/{id}", validActorId)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(validActorId);
  }

  @Test
  public void testGetActorByIdForActorBadRequest() throws IOException, ParseException {
    webClient.get()
        .uri("/api/actors/{id}", "tt0000002")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void testGetActorByIdForActorNotFound() throws IOException, ParseException {
    webClient.get()
        .uri("/api/actors/{id}", "nm111111")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void testActors() throws IOException, ParseException {
    webClient.get().uri("/api/actors")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Actor.class);
  }

}



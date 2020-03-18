package com.microsoft.cse.helium.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@AutoConfigureWebTestClient()
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:application.properties")

@SpringBootTest(properties = {"helium.keyvault.name=${KeyVaultName}", "helium.environment.flag=${He_EnvironmentFlag}"})

public class SecretControllerTest {
  @Autowired
  private WebTestClient webClient;

  @Test
  public void testGetSecret() {
    webClient.get().uri("/api/secret")
        .header(HttpHeaders.ACCEPT, TEXT_PLAIN_VALUE)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class);
  }

}



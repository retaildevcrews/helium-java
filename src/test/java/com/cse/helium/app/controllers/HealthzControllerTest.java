package com.cse.helium.app.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "20000")
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@SpringBootTest
public class HealthzControllerTest {
  @Autowired private WebTestClient webClient;

  @Test
  public void testHealthz() {
    webClient.get().uri("/healthz").exchange().expectStatus().isOk();
  }

  @Test
  public void testHealthzIeTf() {
    webClient.get().uri("/healthz/ietf").exchange().expectStatus().isOk();
  }
}

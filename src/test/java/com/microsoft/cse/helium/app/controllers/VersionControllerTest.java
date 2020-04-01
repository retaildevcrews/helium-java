package com.microsoft.cse.helium.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.*;
import com.microsoft.cse.helium.app.models.HeliumConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HeliumConfig.class)
public class VersionControllerTest {

  @Mock
  private VersionController versionController;

  @Test
  public void testVersion() {
    assertTrue(versionController != null);
    WebTestClient webClient = WebTestClient.bindToController(versionController)
        .configureClient()
        .baseUrl("/")
        .build();

    assertTrue(webClient != null);
    webClient
        .get()
        .uri("/version")
        .exchange()
        .expectStatus()
        .isOk();
  }


}



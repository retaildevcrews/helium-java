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
@SpringBootTest(properties = {"helium.keyvault.name=${KeyVaultName}", "helium.environment.flag=${He_EnvironmentFlag}"})

public class GenreControllerTest {
  @Autowired
  private WebTestClient webClient;

    @Test
    public void testGenres(){
        webClient.get().uri("/api/genres")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class);


    }

}



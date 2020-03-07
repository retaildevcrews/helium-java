package com.microsoft.cse.helium.app.controllers;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.*;

import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.models.HeliumConfig;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=HeliumConfig.class)
//@org.springframework.beans.factory.annotation.Autowired(required=true)

//@SpringApplicationConfiguration(classes={VersionController.class, BuildConfig.class})
//@AutoConfigureWebTestClient
public class VersionControllerTest{

    @Mock
    private VersionController versionController;

    // @InjectMocks
    // private BuildConfig buildConfig;

    //@InjectMocks
    //private BuildProperties buildProperties;

    // @Autowired
    // private WebTestClient webClient;


    private static final Logger _logger = LoggerFactory.getLogger(VersionControllerTest.class);

    @Test
    public void testVersion(){
        assertTrue(versionController != null);
        WebTestClient webClient = WebTestClient.bindToController(versionController)
        .configureClient()
        .baseUrl("/")
        .build();
        //BuildConfig buildConfig = mock (BuildConfig.class);
        //buildConfig.getBuildVersion();
        // assertTrue(buildConfig != null);
        // _logger.error("Hello Sanjeev " + buildConfig.getBuildVersion());
        //webClient = new WebTestClient();
        assertTrue(webClient != null);
        webClient
        .get()
        .uri("/version")
        .exchange()
        .expectStatus()
        .isOk();
        _logger.error("hola hola sanjeev");
    }



}



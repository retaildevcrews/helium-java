package com.microsoft.cse.helium.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
@WebFluxTest
public class VersionControllerTest{

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testVersion(){
        assertTrue(true);
    }

}



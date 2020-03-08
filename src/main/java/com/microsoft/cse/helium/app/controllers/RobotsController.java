package com.microsoft.cse.helium.app.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import org.springframework.http.server.reactive.ServerHttpResponse;

import reactor.core.publisher.Mono;
  
@RestController
public class RobotsController{
    @RequestMapping(value = "/robots*.txt")
    public Mono<String> robots(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.OK);
        return Mono.just("User-agent: *\nDisallow: /\n");
    }
}

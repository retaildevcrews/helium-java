package com.microsoft.cse.helium.app.controllers;

// import com.microsoft.applicationinsights.core.dependencies.apachecommons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
 
import org.springframework.http.server.reactive.ServerHttpResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
  
@RestController
public class RobotsController{
    @RequestMapping(value = "/robots*.txt")
    public Mono<String> robots(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.OK);
        return Mono.just("User-agent: *\nDisallow: /\n");
    }
}

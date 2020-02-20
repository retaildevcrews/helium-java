package com.microsoft.cse.helium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import com.microsoft.cse.helium.models.BuildConfig;;

@RestController
public class VersionController {

  @Autowired
  BuildConfig buildConfig;
  
  @GetMapping(name="Helium Version Controller", 
      value="/version",
      produces = MediaType.TEXT_PLAIN_VALUE)
      
  public Flux<String> version(ServerHttpResponse response) {
    response.setStatusCode(HttpStatus.OK);
    return Flux.just(buildConfig.getBuildVersion());
  }
}

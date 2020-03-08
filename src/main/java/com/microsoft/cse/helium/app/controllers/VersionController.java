package com.microsoft.cse.helium.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import com.microsoft.cse.helium.app.config.BuildConfig;;

@RestController
public class VersionController {

  @Autowired
  BuildConfig buildConfig;
  
  @GetMapping(name="Helium Version Controller", 
      value="/version",
      produces = MediaType.TEXT_PLAIN_VALUE)
      
  public Mono<String> version(ServerHttpResponse response) {
    response.setStatusCode(HttpStatus.OK);
    return Mono.just(buildConfig.getBuildVersion());
  }
}

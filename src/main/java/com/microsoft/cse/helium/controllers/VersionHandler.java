package com.microsoft.cse.helium.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import com.microsoft.cse.helium.models.BuildConfig;;

@Component
public class VersionHandler {

  @Autowired
  BuildConfig buildConfig;
  
  public Mono<ServerResponse> version(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue(buildConfig.getBuildVersion()));
  }
}

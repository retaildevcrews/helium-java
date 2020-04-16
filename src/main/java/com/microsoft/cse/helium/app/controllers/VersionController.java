package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.config.BuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@RestController
public class VersionController {
  private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

  @Autowired
  ApplicationContext context;

  /**
   * Returns the application build version.
   *
   * @param response ServerHttpResponse passed into the alternate version handler by Spring
   * @return Mono/<String/> container the build number
  */
  @GetMapping(name = "Helium Version Controller",
      value = "/version",
      produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<String> version(ServerHttpResponse response) {
    try { 
      response.setStatusCode(HttpStatus.OK);
      return Mono.just(context.getBean(BuildConfig.class).getBuildVersion());
    } catch (Exception ex) {

      logger.error("Error recieved in VersionController", ex);
      return Mono.error(new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "version Error"));
    }
  }
}

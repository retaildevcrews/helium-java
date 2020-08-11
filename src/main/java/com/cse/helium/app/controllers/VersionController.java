package com.cse.helium.app.controllers;


import com.cse.helium.app.config.BuildConfig;
import com.cse.helium.app.config.SwaggerConfig;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
  private static final Logger logger =   LogManager.getLogger(VersionController.class);

  @Autowired
  ApplicationContext context;

  @Autowired
  SwaggerConfig swaggerConfig;

  /**
   * Returns the application build version.
   *
   * @param response ServerHttpResponse passed into the alternate version handler by Spring
   * @return Mono{@literal <}Map{@literal <}String, 
   *      String{@literal <}{@literal <} container the build number
  */
  @GetMapping(name = "Helium Version Controller",
      value = "/version",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Map<String, String>> version(ServerHttpResponse response) {
    try {
      // build the json result body
      LinkedHashMap<String, String> versionResult = new LinkedHashMap<>();

      versionResult.put("apiVersion", swaggerConfig.getInfo().get("version"));
      versionResult.put("appVersion", 
          context.getBean(BuildConfig.class).getBuildVersion());

      response.setStatusCode(HttpStatus.OK);
      return Mono.just(versionResult);
    } catch (Exception ex) {
      logger.error("Error received in VersionController", ex);
      return Mono.error(new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "version Error"));
    }
  }
}

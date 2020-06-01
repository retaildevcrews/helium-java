package com.cse.helium.app.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** RestController. Redirect index.html to swagger. */
@RestController
public class IndexController {

  /** indexController. redirect index.html to swagger so website always serves something. */
  @GetMapping("/")
  @SuppressFBWarnings({"DMI_HARDCODED_ABSOLUTE_FILENAME", "OS_OPEN_STREAM"})
  public ResponseEntity<InputStreamResource> indexController(ServerHttpResponse response) 
      throws IOException {
    response.setStatusCode(HttpStatus.OK);
    Resource resource = new ClassPathResource("static/swagger-ui.html");
    InputStreamResource inputStreamResource = new InputStreamResource(resource.getInputStream());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentLength(resource.getInputStream().available());
    httpHeaders.setContentType(MediaType.TEXT_HTML);
    return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
  }
}

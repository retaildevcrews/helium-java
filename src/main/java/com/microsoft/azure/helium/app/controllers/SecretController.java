package com.microsoft.azure.helium.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping (value = "api/secret")
class SecretController{

    @GetMapping(value="/", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> getSecret(){
        String returnValue = "Return from getSecret()";

        return Mono.just(ResponseEntity.ok()
            .body(returnValue));
    }

    /* Example with .header() */
    /*
    public Mono<ResponseEntity<String>> getSecret(){
        String contentTypeKey = "Content-Type";
        String contentTypeValue = "text/html";
        String returnValue = "Return from getSecret()";

        return Mono.just(ResponseEntity.ok()
            .header(contentTypeKey, contentTypeValue)    
            .body(returnValue));
    }
    */
}
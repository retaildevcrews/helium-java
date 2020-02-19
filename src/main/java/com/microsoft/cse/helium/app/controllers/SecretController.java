package com.microsoft.cse.helium.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

@RestController
@RequestMapping (value = "api/secret")
class SecretController{

    private static final Logger logger = LoggerFactory.getLogger(SecretController.class);

    @GetMapping(value={"/",""}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> getSecret(){

        String returnValue = "Return from getSecret()";

        MSICredentials credentials = new MSICredentials(AzureEnvironment.AZURE);
        KeyVaultClient keyVaultClient = new KeyVaultClient(credentials);
        SecretBundle secret = keyVaultClient.getSecret("https://jf2he.vault.azure.net","CosmosKey","9228944c9038476f8f7d1be86c333c20");


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
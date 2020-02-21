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
import com.microsoft.azure.credentials.AzureCliCredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

@RestController
@RequestMapping (value = "api/secret")
class SecretController{

    private static final Logger logger = LoggerFactory.getLogger(SecretController.class);

    @Autowired
    private KeyVaultService _keyVaultService;

    @GetMapping(value={"/",""}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> getSecret(){

        String returnValue="";

        try{
   
            String secretValue = _keyVaultService.getSecret("CosmosKey");
            returnValue = secretValue;
        }
        catch (Exception ex)
        {
            logger.error("Error~SecretController~" + ex.getMessage());
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return Mono.just(ResponseEntity.ok()
            .body(returnValue));
    }
}
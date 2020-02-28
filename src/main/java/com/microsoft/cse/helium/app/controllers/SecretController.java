package com.microsoft.cse.helium.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Value;

import reactor.core.publisher.Mono;

import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

@RestController
@RequestMapping (value = "api/secret")
public class SecretController{

    private static final Logger _logger = LoggerFactory.getLogger(SecretController.class);

    @Autowired
    private IKeyVaultService _keyVaultService;

    @Value("${helium.keyvault.secretName}")
    private String _secretName;

    @GetMapping(value={"/",""}, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> getSecret(){

        String returnValue="";
        try{
   
            Mono<String> secretValue = _keyVaultService.getSecret(_secretName);

            return _keyVaultService.getSecret(_secretName)
                .map(ResponseEntity::ok)
                .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        catch (Exception ex)
        {
            _logger.error("Error:SecretController:" + ex.getMessage());
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    public IKeyVaultService getKeyVaultService(){
        return _keyVaultService;
    }
}
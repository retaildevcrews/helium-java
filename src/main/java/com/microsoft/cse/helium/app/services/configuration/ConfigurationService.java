package com.microsoft.cse.helium.app.services.configuration;

import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

import lombok.Getter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

import org.springframework.context.annotation.Bean;

import com.microsoft.azure.keyvault.models.CertificateBundle;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;

import reactor.core.publisher.Mono;

@Service
public class ConfigurationService implements IConfigurationService
{
    private static final Logger _logger = LoggerFactory.getLogger(ConfigurationService.class);

    private IKeyVaultService _keyVaultService;

    @Autowired
    public ConfigurationService(IKeyVaultService kvService){
        _keyVaultService = kvService;
        Dictionary<String, String> secrets = _keyVaultService.getSecrets();
        configEntries = secrets;
    }

    @Getter
    Dictionary configEntries = new Hashtable<String, String>();
   
}
package com.microsoft.cse.helium.app.services.configuration;

import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConfigurationService implements IConfigurationService
{
    private static final Logger _logger = LoggerFactory.getLogger(ConfigurationService.class);

    private IKeyVaultService _keyVaultService;

    Map<String, String> _configEntries = new ConcurrentHashMap<String, String>();

    public Map<String, String> getConfigEntries(){
        return _configEntries;
    }

    @Autowired
    public ConfigurationService(IKeyVaultService kvService){
        try{
            _keyVaultService = kvService;
            Map<String, String> secrets = _keyVaultService.getSecretsSync();
            _configEntries = secrets;
        }
        catch (Exception ex){
            _logger.error(ex.getMessage());
            throw(ex);
        }
    }


}
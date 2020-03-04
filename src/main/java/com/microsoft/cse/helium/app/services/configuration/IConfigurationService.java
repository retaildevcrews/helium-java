package com.microsoft.cse.helium.app.services.configuration;

import java.util.*;

import lombok.AccessLevel;
import lombok.Getter;

//import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
//import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;

public interface IConfigurationService {

    //public CosmosDBConfig getConfig();
    public Dictionary<String, String> getConfigEntries();
}

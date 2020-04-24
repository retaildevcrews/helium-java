package com.cse.helium.app.config;

import com.azure.data.cosmos.ConnectionPolicy;
import com.azure.data.cosmos.ConsistencyLevel;
import com.azure.data.cosmos.RetryOptions;
import com.azure.data.cosmos.internal.RequestOptions;
import com.cse.helium.app.Constants;
import com.cse.helium.app.services.configuration.IConfigurationService;
import com.microsoft.azure.spring.data.cosmosdb.config.AbstractCosmosConfiguration;
import com.microsoft.azure.spring.data.cosmosdb.config.CosmosDBConfig;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableCosmosRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCosmosRepositories(basePackages = "com.microsoft.azure.helium.app.*")
public class CosmosDbConfig extends AbstractCosmosConfiguration {

  private static final Logger logger = LogManager.getLogger(CosmosDbConfig.class);

  protected IConfigurationService configurationService;

  protected final RequestOptions requestOptions = new RequestOptions();

  /**
   * CosmosDBConfig.
   */
  @Autowired
  public CosmosDbConfig(IConfigurationService configService) {
    configurationService = configService;

    requestOptions.setConsistencyLevel(ConsistencyLevel.CONSISTENT_PREFIX);
    requestOptions.setScriptLoggingEnabled(true);
  }

  /**
   * CosmosDBConfig.
   */
  @Bean
  @Primary
  public CosmosDBConfig buildCosmosDbConfig() {
    try {

      String uri = configurationService.getConfigEntries().get(Constants.COSMOS_URL_KEYNAME);
      String key = configurationService.getConfigEntries().get(Constants.COSMOS_KEY_KEYNAME);
      String dbName = configurationService.getConfigEntries()
          .get(Constants.COSMOS_DATABASE_KEYNAME);

      ConnectionPolicy policy = new ConnectionPolicy();
      RetryOptions retryOptions = new RetryOptions();
      retryOptions.maxRetryWaitTimeInSeconds(60);
      policy.retryOptions(retryOptions);

      return CosmosDBConfig.builder(uri, key, dbName)
          .requestOptions(requestOptions).connectionPolicy(policy)
          .build();
    } catch (Exception ex) {
      logger.error("buildCosmosDbConfig failed with error: " + ex.getMessage());

      throw ex;
    }
  }
}
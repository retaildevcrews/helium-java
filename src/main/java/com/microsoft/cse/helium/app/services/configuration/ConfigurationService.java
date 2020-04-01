package com.microsoft.cse.helium.app.services.configuration;

import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService implements IConfigurationService {
  private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

  private IKeyVaultService keyVaultService;

  Map<String, String> configEntries = new ConcurrentHashMap<String, String>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /**
   * ConfigurationService.
   */
  @Autowired
  public ConfigurationService(IKeyVaultService kvService) {
    try {
      keyVaultService = kvService;
      logger.info("keyVaultService is " + (keyVaultService == null ? "NULL" : "NOT NULL"));
      Map<String, String> secrets = keyVaultService.getSecretsSync();
      logger.info("Secrets are " + (secrets == null ? "NULL" : "NOT NULL"));
      configEntries = secrets;
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      throw ex;
    }
  }
}
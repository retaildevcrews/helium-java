package com.cse.helium.app.services.configuration;

import com.cse.helium.app.services.keyvault.IKeyVaultService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService implements IConfigurationService {
  private static final Logger logger =   LogManager.getLogger(ConfigurationService.class);

  private IKeyVaultService keyVaultService;

  Map<String, String> configEntries = new ConcurrentHashMap<>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /**
   * ConfigurationService.
   */
  @SuppressFBWarnings("DM_EXIT")
  @Autowired
  public ConfigurationService(IKeyVaultService kvService) {
    try {
      if (kvService == null) {
        logger.info("keyVaultService is null");
        System.exit(-1);
      }

      keyVaultService = kvService;
      Map<String, String> secrets = keyVaultService.getSecrets();
      if (logger.isInfoEnabled()) {
        logger.info(MessageFormat.format("Secrets are {0}", secrets == null ? "NULL" : "NOT NULL"));
      }
      configEntries = secrets;

    } catch (Exception ex) {
      logger.error(ex.getMessage());
      throw ex;
    }
  }
}
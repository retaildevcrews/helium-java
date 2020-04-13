package com.microsoft.cse.helium.app.services.configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Service;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Service
public class ConfigurationService implements IConfigurationService {
  private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

  @Autowired
  ApplicationContext context;

  private IKeyVaultService keyVaultService;

  Map<String, String> configEntries = new ConcurrentHashMap<String, String>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /**
   * ConfigurationService.
   */
  @SuppressFBWarnings("DM_EXIT")
  @Autowired
  public ConfigurationService(IKeyVaultService kvService,
                              ApplicationArguments applicationArguments) {
    try {
      if (kvService == null) {
        logger.info("keyVaultService is null");
        System.exit(-1);
      }

      keyVaultService = kvService;
      Map<String, String> secrets = keyVaultService.getSecretsSync();
      logger.info("Secrets are " + (secrets == null ? "NULL" : "NOT NULL"));
      configEntries = secrets;

      if (applicationArguments != null) {
        SimpleCommandLinePropertySource commandLinePropertySource =
            new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
        Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
          if (s.equals("dry-run") || s.equals("d")) {
            //setAuthType(commandLinePropertySource.getProperty(s));
          }
        });
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      throw ex;
    }
  }

  void PrintDryRunParameters() {
    // Console.WriteLine($"Version            {Middleware.VersionExtensions.Version}");
    // Console.WriteLine($"Keyvault           {kvUrl}");
    // Console.WriteLine($"Auth Type          {authType}");
    // Console.WriteLine($"Cosmos Server      {config.GetValue<string>(Constants.CosmosUrl)}");
    // Console.WriteLine($"Cosmos Key         Length({config.GetValue<string>(Constants.CosmosKey).Length})");
    // Console.WriteLine($"Cosmos Database    {config.GetValue<string>(Constants.CosmosDatabase)}");
    // Console.WriteLine($"Cosmos Collection  {config.GetValue<string>(Constants.CosmosCollection)}");
    // Console.WriteLine($"App Insights Key   {(string.IsNullOrEmpty(config.GetValue<string>(Constants.AppInsightsKey)) ? "(not set" : "Length(" + config.GetValue<string>(Constants.AppInsightsKey).Length.ToString(CultureInfo.InvariantCulture))})");
    System.out.println("Version                   {1}",
        context.getBean(BuildConfig.class).getBuildVersion());

  }
}
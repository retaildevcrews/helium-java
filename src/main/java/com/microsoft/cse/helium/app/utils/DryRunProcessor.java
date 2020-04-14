package com.microsoft.cse.helium.app.utils;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.services.keyvault.IEnvironmentReader;
import com.microsoft.cse.helium.app.services.keyvault.IKeyVaultService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

/**
 * DryRunProcessor.
 */
@Component
public class DryRunProcessor {

  @Autowired
  ApplicationArguments applicationArguments;

  @Autowired
  private BuildConfig buildConfig;

  @Autowired
  IKeyVaultService keyVaultService;

  @Autowired
  IEnvironmentReader environmentReader;

  private static final Logger logger = LoggerFactory.getLogger(DryRunProcessor.class);

  /**
   * onApplicationEvent.
   */
  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {

    logger.info("Application Context has been fully started up");
    logger.info("All beans are now instantiated and ready to go!");
    if (applicationArguments != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("dry-run") || s.equals("d")) {
          printDryRunParameters();
          System.exit(0);
        }
      });
    }
  }

  @SuppressFBWarnings({"NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD"})
  void printDryRunParameters() {
    System.out.println(MessageFormat.format("Version                    {0}",
        buildConfig.getBuildVersion()));
    System.out.println(MessageFormat.format("Auth Type                  {0}",
        environmentReader.getAuthType()));
    System.out.println(MessageFormat.format("Cosmos Server              {0}",
        keyVaultService.getSecret(Constants.COSMOS_URL_KEYNAME).block()));

    String cosmosKey = keyVaultService.getSecret(Constants.COSMOS_KEY_KEYNAME).block();

    System.out.println(MessageFormat.format("Cosmos Key                 {0}",
        cosmosKey == null || cosmosKey.isEmpty() ? "(not set)".length()
        : cosmosKey.length()));

    System.out.println(MessageFormat.format("Cosmos Database            {0}",
        keyVaultService.getSecret(Constants.COSMOS_DATABASE_KEYNAME).block()));
    System.out.println(MessageFormat.format("Cosmos Collection          {0}",
        keyVaultService.getSecret(Constants.COSMOS_COLLECTION_KEYNAME).block()));

    String appInsightsKey = keyVaultService.getSecret(Constants.APP_INSIGHTS_KEY).block();

    System.out.println(MessageFormat.format("App Insights Key           {0}",
        appInsightsKey == null || appInsightsKey.isEmpty() ? "(not set)".length()
        : appInsightsKey.length()));
  }
}
package com.cse.helium.app.utils;

import com.cse.helium.app.Constants;
import com.cse.helium.app.config.BuildConfig;
import com.cse.helium.app.services.keyvault.EnvironmentReader;
import com.cse.helium.app.services.keyvault.IEnvironmentReader;
import com.cse.helium.app.services.keyvault.IKeyVaultService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import java.util.Arrays;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

/**
 * CommonUtils.
 */
@Component
public class CommonUtils {

  private CommonUtils() {
    // disable constructor for utility class
  }

  /**
   * handleCliLogLevelOption.
   *
   * @param args the log level in string form.
   */
  public static void handleCliLogLevelOption(String[] args) {
    if (args != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(args);
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("log-level") || s.equals("l")) {
          Level level = setLogLevel(commandLinePropertySource.getProperty(s));
          if (level == null) {
            printCmdLineHelp();
            System.exit(-1);
          }
          Configurator.setLevel("com.cse.helium",
              level);
        }
      });
    }
  }

  private static Level setLogLevel(String logLevel) {
    switch (logLevel) {
      case "trace":
        return Level.TRACE;
      case "debug":
        return Level.DEBUG;
      case "info":
        return Level.INFO;
      case "warn":
        return Level.WARN;
      case "error":
        return Level.ERROR;
      case "fatal":
        return Level.FATAL;
      default:
        return null;
    }
  }


  /**
   * validate cli dry run option.
   */
  public static void validateCliDryRunOption(ApplicationArguments applicationArguments,
                                              IKeyVaultService keyVaultService,
                                              BuildConfig buildConfig,
                                              IEnvironmentReader environmentReader) {
    if (applicationArguments != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("dry-run") || s.equals("d")) {
          printDryRunParameters(keyVaultService, buildConfig, environmentReader);
          System.exit(0);
        }
      });
    }
  }

  @SuppressFBWarnings({"NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD"})
  @SuppressWarnings ("squid:S106") // System.out needed to print usage
  static void printDryRunParameters(IKeyVaultService keyVaultService, BuildConfig buildConfig,
                                    IEnvironmentReader environmentReader) {
    System.out.println(MessageFormat.format("Version                    {0}",
        buildConfig.getBuildVersion()));
    System.out.println(MessageFormat.format("Auth Type                  {0}",
        environmentReader.getAuthType()));
    System.out.println(MessageFormat.format("Cosmos Server              {0}",
        keyVaultService.getSecret(Constants.COSMOS_URL_KEYNAME).block()));

    String cosmosKey = keyVaultService.getSecretValue(Constants.COSMOS_KEY_KEYNAME).block();

    System.out.println(MessageFormat.format("Cosmos Key                 {0}",
        cosmosKey == null || cosmosKey.isEmpty() ? "(not set)".length() : cosmosKey.length()));

    System.out.println(MessageFormat.format("Cosmos Database            {0}",
        keyVaultService.getSecret(Constants.COSMOS_DATABASE_KEYNAME).block()));
    System.out.println(MessageFormat.format("Cosmos Collection          {0}",
        keyVaultService.getSecret(Constants.COSMOS_COLLECTION_KEYNAME).block()));

    String appInsightsKey = keyVaultService.getSecretValue(Constants.APP_INSIGHTS_KEY).block();

    System.out.println(MessageFormat.format("App Insights Key           {0}",
        appInsightsKey == null || appInsightsKey.isEmpty() ? "(not set)".length()
            : appInsightsKey.length()));
  }

  /**
   * validateCliKeyvaultAndAuthType - validate the authtype and keyvault name from CLI.
   */
  public static void validateCliKeyvaultAndAuthTypeOption(ApplicationArguments applicationArguments,
                                                          EnvironmentReader environmentReader) {
    if (applicationArguments != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("auth-type")) {
          environmentReader.setAuthType(commandLinePropertySource.getProperty(s));
        } else if (s.equals("keyvault-name")) {
          environmentReader.setKeyVaultName(commandLinePropertySource.getProperty(s));
        } else if (s.equals("help")) {
          CommonUtils.printCmdLineHelp();
          System.exit(0);
        }
      });
    }
  }

  /**
   * prints the command line help.
   */
  @SuppressWarnings ("squid:S106") // System.out needed to print usage
  public static void printCmdLineHelp() {
    System.out.println("\r\nUsage:\r\n"
        + "   mvn clean spring-boot:run \r\n "
        + "\t-Dspring-boot.run.arguments=\" --help \r\n"
        + "\t\t--auth-type=<CLI|MI|VS>\r\n"
        + "\t\t--keyvault-name=<keyVaultName>\r\n"
        + "\t\t--dry-run\r\n"
        + "\t\t--log-level=<trace|info|warn|error|fatal>\"");
  }
}

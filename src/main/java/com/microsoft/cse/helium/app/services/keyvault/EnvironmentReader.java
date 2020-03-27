package com.microsoft.cse.helium.app.services.keyvault;

import com.microsoft.cse.helium.app.Constants;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Service;

/**
 * EnvironmentReader
 * A class to read the environment variables and validate them.
 */
@Service
public class EnvironmentReader implements IEnvironmentReader {

  private String authType;
  private String keyVaultName;
  private final String keyVaultNameRegex = "^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";
  private static final Logger logger = LoggerFactory.getLogger(EnvironmentReader.class);

  /**
   * EnvironmentReader
   * This constructor initializes the class with the arguments provided from the CLI
   * during the Springboot construction of the beans.
   *
   * @param applicationArguments the command line arguments.
   */
  @SuppressFBWarnings("DM_EXIT")
  @Autowired
  public EnvironmentReader(ApplicationArguments applicationArguments) {
    SimpleCommandLinePropertySource commandLinePropertySource =
        new SimpleCommandLinePropertySource(applicationArguments.getSourceArgs());
    Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
      if (s.equals("authtype")) {
        setAuthType(commandLinePropertySource.getProperty(s));
      } else if (s.equals("kvname")) {
        setKeyVaultName(commandLinePropertySource.getProperty(s));
      } else if (s.equals("h")) {
        System.out.println("Usage: mvn clean spring-boot:run  "
            + "-Dspring-boot.run.arguments=\"--h --authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
        System.exit(0);
      }
    });
  }

  @SuppressFBWarnings("DM_EXIT")
  private void setAuthType(String authType) {
    if (authType == null) {
      System.out.println("Usage: mvn clean spring-boot:run  "
          + "-Dspring-boot.run.arguments=\"--h --authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
      System.exit(-1);
    }

    if (authType.equals(Constants.USE_MSI)) {
      this.authType = Constants.USE_MSI;
    } else if (authType.equals(Constants.USE_CLI)) {
      this.authType = Constants.USE_CLI;
    } else {
      System.out.println("Usage: mvn clean spring-boot:run  "
          + "-Dspring-boot.run.arguments=\"--h --authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
      System.exit(-1);
    }
  }

  /**
   * getAuthType.
   *
   * @return returns CLI or MSI.
   */
  public String getAuthType() {
    if (this.authType != null) {
      return this.authType;
    }

    authType = System.getenv(Constants.AUTH_TYPE);

    // If it is not set, use the MSI
    if (authType == null) {
      return Constants.USE_MSI;
    }

    // ONLY If it is set and values are either MSI or CLI, we will accept.
    // otherwise, default is just the MSI
    if (authType.equals(Constants.USE_MSI)) {
      return Constants.USE_MSI;
    } else if (authType.equals(Constants.USE_CLI)) {
      return Constants.USE_CLI;
    } else {
      return Constants.USE_MSI;
    }
  }

  @SuppressFBWarnings("DM_EXIT")
  private void setKeyVaultName(String kvName) {
    if (kvName == null) {
      System.out.println("Usage: mvn clean spring-boot:run  "
          + "-Dspring-boot.run.arguments=\"--h --authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
      System.exit(-1);
    }

    if (!checkKeyVaultName(kvName)) {
      logger.error("helium.keyvault.name (KeyVaultName) value is '" + kvName
          + "' which does not meet the criteria must be 3-24 characters long, begin with a "
          + "character, may contain alphanumeric or hyphen, no repeating hyphens, and end with "
          + "alphanumeric.  Check ${KeyVaultName} in your environment variables.");
      System.out.println("Usage: mvn clean spring-boot:run"
          + "-Dspring-boot.run.arguments=\"--authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
      System.exit(-1);
    }

    this.keyVaultName = kvName;
  }

  /**
   * getKeyVaultName.
   *
   * @return returns the key vault name.
   */
  public String getKeyVaultName() {
    if (this.keyVaultName != null) {
      return this.keyVaultName;
    }

    keyVaultName = System.getenv(Constants.KEY_VAULT_NAME);
    if (keyVaultName == null || StringUtils.isEmpty(keyVaultName)) {
      throw new IllegalArgumentException("keyVaultName");
    }
    return keyVaultName;
  }

  /**
   * checkKeyVaultName.
   */
  private Boolean checkKeyVaultName(final String keyVaultName) {
    Boolean validSetting = true;

    if (keyVaultName.length() < 3 || keyVaultName.length() > 24) {
      validSetting = false;
    }

    // validate key vault name with regex:
    // ^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$
    // ^[a-zA-Z] - start of string, must be a alpha character
    // (?!.*--) - look ahead and make sure there are no double hyphens (e.g., "--")
    // [a-zA-Z0-9-]* - match alphanumeric and hyphen as many times as needed
    // [a-zA-Z0-9] - final character must be alphanumeric
    if (!keyVaultName.matches(keyVaultNameRegex)) {
      validSetting = false;
    }

    return validSetting;
  }


}
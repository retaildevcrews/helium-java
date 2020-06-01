package com.cse.helium.app.services.keyvault;

import com.cse.helium.app.Constants;
import com.cse.helium.app.utils.CommonUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.text.MessageFormat;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

/**
 * EnvironmentReader
 * A class to read the environment variables and validate them.
 */
@Service
public class EnvironmentReader implements IEnvironmentReader {

  private String authType;
  private String keyVaultName;
  private static final String KV_NAME_REGEX = "^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";
  private static final Logger logger =   LogManager.getLogger(EnvironmentReader.class);

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
    CommonUtils.validateCliKeyvaultAndAuthTypeOption(applicationArguments, this);
  }

  /**
   * setAuthType.
   * @param authType set the Auth type from env or cli.
   */
  @SuppressFBWarnings("DM_EXIT")
  public void setAuthType(String authType) {
    if (authType == null) {
      CommonUtils.printCmdLineHelp();
      System.exit(-1);
    }

    if (authType.equals(Constants.USE_MSI)) {
      this.authType = Constants.USE_MSI;
    } else if (authType.equals(Constants.USE_CLI)) {
      this.authType = Constants.USE_CLI;
    } else if (authType.equals(Constants.USE_MSI_APPSVC)) {
      this.authType = Constants.USE_MSI_APPSVC;
    } else if (authType.equals(Constants.USE_VS)) {
      logger.log(Level.ERROR, "VS Credentials are not yet supported in Java");
      System.exit(-1);
    } else {
      CommonUtils.printCmdLineHelp();
      System.exit(-1);
    }
  }

  /**
   * getAuthType.
   *
   * @return returns CLI or MSI.
   */
  @SuppressFBWarnings("DM_EXIT")
  public String getAuthType() {
    if (this.authType != null) {
      if (logger.isInfoEnabled()) {
        logger.info(MessageFormat.format("Auth type is {0}", this.authType));
      }
      return this.authType;
    }

    authType = System.getenv(Constants.AUTH_TYPE);

    // If it is not set, use the MSI
    if (authType == null) {
      logger.info("Auth type is null, defaulting to MSI APP SVC");
      return Constants.USE_MSI_APPSVC;
    }

    // ONLY If it is set and values are either MSI or CLI, we will accept.
    // otherwise, default is just the MSI
    if (authType.equals(Constants.USE_MSI)) {
      logger.info("Auth type is MSI");
      return Constants.USE_MSI;
    } else if (authType.equals(Constants.USE_CLI)) {
      logger.info("Auth type is CLI");
      return Constants.USE_CLI;
    } else if (authType.equals(Constants.USE_VS)) {
      logger.log(Level.ERROR, "VS Credentials are not yet supported in Java");
      System.exit(-1);
      // following return needs to be there because java compiler wants a return statement.
      return null;
    } else {
      logger.info("AUTH_TYPE is not set and could not default. Exiting");
      System.exit(-1);
      // following return needs to be there because java compiler wants a return statement.
      return null;
    }
  }

  /**
   * setKeyVaultName.
   * @param kvName set the key vault name from env or cli.
   */
  @SuppressFBWarnings({"squid:S2629", "DM_EXIT"}) // suppressing conditional error log 
  public void setKeyVaultName(String kvName) {
    if (kvName == null) {
      CommonUtils.printCmdLineHelp();
      System.exit(-1);
    }

    if (Boolean.FALSE.equals(isValidKeyVaultName(kvName))) {
      // suppression of 2629 is not working so wrapped call in conditional
      if (logger.isErrorEnabled()) {
        logger.error(MessageFormat.format(Constants.KEYVAULT_NAME_ERROR_MSG, kvName));
      }
      CommonUtils.printCmdLineHelp();
      System.exit(-1);
    }

    this.keyVaultName = kvName;
  }

  /**
   * getKeyVaultName.
   *
   * @return returns the key vault name.
   */
  @SuppressFBWarnings("DM_EXIT")
  public String getKeyVaultName() {
    if (this.keyVaultName != null) {
      if (logger.isInfoEnabled()) {
        logger.info(MessageFormat.format("KeyVaultName is {0}", this.keyVaultName));
      }
      return this.keyVaultName;
    }

    keyVaultName = System.getenv(Constants.KEY_VAULT_NAME);
    if (keyVaultName == null || !isValidKeyVaultName(keyVaultName)) {
      logger.error("KeyVault name not set. Exiting");
      CommonUtils.printCmdLineHelp();
      System.exit(-1);
    }

    return keyVaultName;
  }

  /**
   * isValidKeyVaultName.
   */
  private Boolean isValidKeyVaultName(final String keyVaultName) {
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
    if (!keyVaultName.matches(KV_NAME_REGEX)) {
      validSetting = false;
    }

    return validSetting;
  }


}
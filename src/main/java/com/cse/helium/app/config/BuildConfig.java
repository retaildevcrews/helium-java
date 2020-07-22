package com.cse.helium.app.config;

import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;

/**
 * BuildConfig.
 */
public class BuildConfig {

  private static final Logger logger =   LogManager.getLogger(BuildConfig.class);

  @Autowired
  ApplicationContext context;

  /**
   * getBuildVersion. Returns build version.
   */
  public String getBuildVersion() {
    String major = context.getBean(BuildProperties.class).getVersion();

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("version {0}", major));
    }
    return major;

  }
}
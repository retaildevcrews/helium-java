package com.microsoft.cse.helium.app.config;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;

/**
 * BuildConfig.
 */
public class BuildConfig {

  private static final Logger logger = LoggerFactory.getLogger(BuildConfig.class);

  @Autowired
  ApplicationContext context;

  /**
   * getBuildVersion. Returns build version.
   */
  public String getBuildVersion() {
    // An Instant represents a moment on the timeline in UTC with a resolution of up to nanoseconds.
    Instant buildTime = context.getBean(BuildProperties.class).getTime();

    // major.minor.MMd.hhmm
    String major = context.getBean(BuildProperties.class).getVersion();

    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("MMdd.hhmm")
        .withZone(ZoneId.of("UTC"));

    String formattedDateTime = formatter.format(buildTime);

    logger.info("version " + major + "." + formattedDateTime);
    return major + "+" + formattedDateTime;

  }
}
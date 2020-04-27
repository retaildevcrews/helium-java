package com.cse.helium.app.utils;

import com.cse.helium.app.config.BuildConfig;
import com.cse.helium.app.services.keyvault.IEnvironmentReader;
import com.cse.helium.app.services.keyvault.IKeyVaultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
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

  private static final Logger logger = LogManager.getLogger(DryRunProcessor.class);

  /**
   * onApplicationEvent.
   */
  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    logger.info("Application Context has been fully started up");
    logger.info("All beans are now instantiated and ready to go!");
    CommonUtils.validateCliDryRunOption(applicationArguments,
        keyVaultService, buildConfig, environmentReader);
  }

}
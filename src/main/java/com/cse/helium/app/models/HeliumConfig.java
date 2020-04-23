package com.cse.helium.app.models;

import com.cse.helium.app.config.BuildConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeliumConfig {
  @Bean
  public BuildConfig buildConfig() {
    return new BuildConfig();
  }
}
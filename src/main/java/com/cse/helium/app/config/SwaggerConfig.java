package com.cse.helium.app.config;

import com.cse.helium.app.services.configuration.JSONConfigReader;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
  value = "classpath:/static/swagger/helium.json",
  factory = JSONConfigReader.class
)
@ConfigurationProperties
public class SwaggerConfig {

  private LinkedHashMap<String, String> info;

  public Map<String, String> getInfo() {
    return this.info;
  }

  public void setInfo(Map<String, String> info) {
    this.info = (LinkedHashMap<String, String>) info;
  }
}
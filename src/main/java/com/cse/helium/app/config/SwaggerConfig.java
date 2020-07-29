package com.cse.helium.app.config;

import com.cse.helium.app.services.configuration.JsonConfigReader;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    value = "classpath:/static/swagger/helium.json",
    factory = JsonConfigReader.class
)
@ConfigurationProperties
public class SwaggerConfig {

  /* info stores the 'info' dictionary value from helium.json */
  private Map<String, String> info;

  public Map<String, String> getInfo() {
    return this.info;
  }

  public void setInfo(Map<String, String> info) {
    this.info = info;
  }
}
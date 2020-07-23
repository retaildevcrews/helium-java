package com.cse.helium.app.services.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class JsonConfigReader implements PropertySourceFactory {

  /**
   * createPropertySource makes use of the Jackson JSON mapping library
   *    to load a JSON config file.
   *
   * @return PropertySource{@literal <}?{@literal >}
  */
  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource)
      throws IOException {
    Map<String, Object> jsonReader = new ObjectMapper().readValue(
        resource.getInputStream(), new TypeReference<Map<String, Object>>(){});
    return new MapPropertySource("json-config-reader", jsonReader);
  }
}
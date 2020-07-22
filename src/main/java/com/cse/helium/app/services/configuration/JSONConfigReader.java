package com.cse.helium.app.services.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class JSONConfigReader implements PropertySourceFactory {
	
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource)
		throws IOException {
		Map<String, Object> jsonReader = new ObjectMapper().readValue(
			resource.getInputStream(), new TypeReference<Map<String, Object>>(){});
		return new MapPropertySource("json-config-reader", jsonReader);
	}
}
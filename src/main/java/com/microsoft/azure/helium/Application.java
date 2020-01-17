package com.microsoft.azure.helium;

import com.microsoft.applicationinsights.TelemetryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	Environment environment;

	@Value("${azure.keyvault.uri}")
	private String keyUri;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		TelemetryConfiguration configuration = TelemetryConfiguration.getActive();
	}

	public void run(String... varl) throws Exception {
		logger.info("keyUri: " + keyUri);

	}



}

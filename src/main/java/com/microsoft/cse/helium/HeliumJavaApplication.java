package com.microsoft.cse.helium;

import com.microsoft.cse.helium.app.services.configuration.IConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableWebFlux 
@EnableSwagger2WebFlux
@ComponentScan("com.microsoft.cse.helium")
public class HeliumJavaApplication {

	public static void main(String[] args) {

		SpringApplication.run(HeliumJavaApplication.class, args);
	}



}

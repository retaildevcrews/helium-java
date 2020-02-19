package com.microsoft.cse.helium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
// Imports the Spring Web Reactive Config from Webflux config support
@EnableWebFlux 
@ComponentScan("com.microsoft.cse.helium")
public class HeliumJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeliumJavaApplication.class, args);
	}



}

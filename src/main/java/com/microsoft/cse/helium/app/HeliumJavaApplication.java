package com.microsoft.cse.helium.app;

import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableSwagger2WebFlux
@ComponentScan("com.microsoft.cse.helium")
public class HeliumJavaApplication {

  private static final Logger logger = LoggerFactory.getLogger(KeyVaultService.class);

  public static void main(String[] args) {

    System.out.println("Helium Startup Sequence: Printing all arguments NOW!!");
    for(String arg:args) {
      System.out.println(arg);
    }

    SpringApplication.run(HeliumJavaApplication.class, args);
  }
}

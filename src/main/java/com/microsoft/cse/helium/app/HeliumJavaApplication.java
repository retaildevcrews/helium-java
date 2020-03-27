package com.microsoft.cse.helium.app;

import java.util.Arrays;
import java.util.List;

import com.microsoft.cse.helium.app.services.keyvault.IEnvironmentReader;
import com.microsoft.cse.helium.app.services.keyvault.KeyVaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties.Undertow.Options;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableSwagger2WebFlux
@ComponentScan("com.microsoft.cse.helium")
public class HeliumJavaApplication {

  private static IEnvironmentReader environmentReader;

  private static final Logger logger = LoggerFactory.getLogger(KeyVaultService.class);

  public static void main(String[] args) {

    logger.info("Helium Startup Sequence: Printing all arguments NOW!!");

    SimpleCommandLinePropertySource commandLinePropertySource = new SimpleCommandLinePropertySource(args);
    Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
      if(s.equals("--authtype")) {
        environmentReader.setAuthType(commandLinePropertySource.getProperty(s));
      } else if (s.equals("--kvname ")) {
        environmentReader.setKeyVaultName(commandLinePropertySource.getProperty(s));
      } else if (s.equals("--h")) {
        System.out.println("Usage: mvn clean spring-boot:run  "
        + "-Dspring-boot.run.arguments=\"--authtype=<CLI|MSI> --kvname=<keyVaultName>\"");
        System.exit(0);
      }
    });

    SpringApplication.run(HeliumJavaApplication.class, args);
  }
}

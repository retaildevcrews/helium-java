package com.cse.helium.app;

import java.util.Arrays;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableSwagger2WebFlux
@ComponentScan("com.cse.helium")
public class HeliumJavaApplication {

  /**
  * main.
  */
  public static void main(String[] args) {
    if (args != null) {
      SimpleCommandLinePropertySource commandLinePropertySource =
          new SimpleCommandLinePropertySource(args);
      Arrays.stream(commandLinePropertySource.getPropertyNames()).forEach(s -> {
        if (s.equals("log-level") || s.equals("l")) {
          Configurator.setLevel("com.cse.helium",
              setLogLevel(commandLinePropertySource.getProperty(s)));
        }
      });
    }

    SpringApplication.run(HeliumJavaApplication.class, args);
  }

  private static Level setLogLevel(String logLevel) {
    switch (logLevel) {
      case "trace":
        return Level.TRACE;
      case "debug":
        return Level.DEBUG;
      case "info":
        return Level.INFO;
      case "warn":
        return Level.WARN;
      case "error":
        return Level.ERROR;
      case "fatal":
        return Level.FATAL;
      default:
        System.out.println("Usage: mvn clean spring-boot:run  "
            + "-Dspring-boot.run.arguments=\"--h --auth-type=<CLI|MSI|VS>"
            + " -keyvault-name=<keyVaultName>"
            + "--log-level=<trace|info|warn|error|fatal>\"");
        System.exit(0);
        return null; // java compiler needs this to satisfy compilation errors
    }
  }
}

package com.cse.helium.app;

import com.cse.helium.app.utils.CommonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
    
    CommonUtils.handleCliLogLevelOption(args);
    SpringApplication.run(HeliumJavaApplication.class, args);
  }
}

package com.microsoft.cse.helium.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * SwaggerConfig.
 */
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {
  @Autowired
  private BuildConfig buildConfig;

  /**
   * api.
   */
  @Bean
  public Docket api() {
    // return new Docket(DocumentationType.SWAGGER_2)
    //.host("localhost").pathProvider(new PathProvider() {
    //   private final String ROOT = "/";

    //   @Override
    //   public String getOperationPath(String operationPath) {
    //     return operationPath.replace(ROOT, "");
    //     // return "";
    //   }

    //   @Override
    //   public String getResourceListingPath(String groupName, String apiDeclaration) {
    //     return "/static";
    //   }
    // });


    // String host = environment.getRequiredProperty("application.apiBaseFullPath");
    // if (host.contains("://")) {
    //  host = host.substring(host.indexOf("://") + 3, host.length());
    // }
    // return new Docket(DocumentationType.SWAGGER_2)
    //   //.host(host)
    //   .pathProvider(new CustomPathProvider())
    //   .select()
    //   .apis(RequestHandlerSelectors.any())
    //   .paths(PathSelectors.any())
    //   .build();


    // .select()
    // .apis(RequestHandlerSelectors.any())
    // .paths(PathSelectors.any())
    // .build()
    // .apiInfo(apiInfo()).useDefaultResponseMessages(false);

    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo()).useDefaultResponseMessages(false);


    //}

    // private class CustomPathProvider extends AbstractPathProvider {
    //   @Override
    //   protected String applicationPath() {
    //     return "/";
    //   }

    //   @Override
    //   protected String getDocumentationPath() {
    //     return "/swagger.json";
    //   }
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Helium(Java)")
        .description("Java app for bootstrapping your next Web Apps for Containers service using "
            + "Key Vault and Managed Identities")
        .version(buildConfig.getBuildVersion())
        .build();
  }
}
package com.microsoft.cse.helium.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    // force all static file requests into the /static folder where we have added all the
    // files needed for swagger statically from the swagger-dist.
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/");
  }
}

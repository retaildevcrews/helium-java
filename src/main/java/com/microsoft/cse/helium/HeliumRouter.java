package com.microsoft.cse.helium;

import com.microsoft.cse.helium.controllers.VersionHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration 

public class HeliumRouter {  
    @Bean  
    public RouterFunction<ServerResponse> routes(TestHandler handler, VersionHandler versionHandler) {  
  
        return RouterFunctions  
            .route(RequestPredicates  
                .GET("/").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::hello)
                .andRoute(RequestPredicates.GET("/version").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), versionHandler::version);
    }  
 
}
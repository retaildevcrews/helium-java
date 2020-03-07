package com.microsoft.cse.helium.app.models;

import com.microsoft.cse.helium.app.config.BuildConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeliumConfig {
    @Bean
    public BuildConfig buildConfig() {
        return new BuildConfig();
    }
}
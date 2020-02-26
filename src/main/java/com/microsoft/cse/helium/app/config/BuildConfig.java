package com.microsoft.cse.helium.app.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
public class BuildConfig {

    private static final Logger logger = LoggerFactory.getLogger(BuildConfig.class);

    @Autowired
    BuildProperties buildProperties;

    public String getBuildVersion(){
        // An Instant represents a moment on the timeline in UTC with a resolution of up to nanoseconds.
        Instant buildTime = buildProperties.getTime();
        
        // major.minor.MMd.hhmm
        String major = buildProperties.getVersion();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd.hhmm").withZone(ZoneId.of("UTC"));
        String formattedDateTime = formatter.format(buildTime);

        logger.info("version" + buildProperties.getTime() + "." + formattedDateTime);
        return  major + "." + formattedDateTime;

    }
}
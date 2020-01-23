package com.microsoft.azure.helium.config;


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
        //An Instant represents a moment on the timeline in UTC with a resolution of up to nanoseconds.
        Instant buildTime = buildProperties.getTime();
        //major.minor.MMd.hhmm
        String major = buildProperties.getVersion();
        DateTimeFormatter DATE_TIME_FORMATTER1 = DateTimeFormatter.ofPattern("MMDD").withZone( ZoneId.of("UTC"));
        String MMDD = DATE_TIME_FORMATTER1.format(buildTime);
        DateTimeFormatter DATE_TIME_FORMATTER2 = DateTimeFormatter.ofPattern("hhmm").withZone( ZoneId.of("UTC"));
        String hhmm = DATE_TIME_FORMATTER2.format(buildTime);
        logger.info("version" + buildProperties.getTime() + "." + DATE_TIME_FORMATTER1.format(buildTime)  + "." + DATE_TIME_FORMATTER2.format(buildTime));
        return  major + "." + MMDD + "." + hhmm;

    }
}

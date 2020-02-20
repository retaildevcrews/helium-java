
package com.microsoft.cse.helium.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
public class BuildConfig {

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
        
        return  major + "." + MMDD + "." + hhmm;

    }
}

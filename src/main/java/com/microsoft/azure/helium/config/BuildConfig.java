package com.microsoft.azure.helium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class BuildConfig {

    @Autowired
    BuildProperties buildProperties;

    public String getBuildVersion(){

        Instant buildTime = buildProperties.getTime();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        System.out.println(DATE_TIME_FORMATTER.format(buildTime));

        //major.minor.MMdd.hhmm
        String major = buildProperties.getVersion();
        DateTimeFormatter DATE_TIME_FORMATTER1 = DateTimeFormatter.ofPattern("MMDD")
                .withZone(ZoneId.systemDefault());
        String MMDD = DATE_TIME_FORMATTER1.format(buildTime);

        DateTimeFormatter DATE_TIME_FORMATTER2 = DateTimeFormatter.ofPattern("hhmm")
                .withZone(ZoneId.systemDefault());
        String hhmm = DATE_TIME_FORMATTER2.format(buildTime);

        String versionNo = major + "." + MMDD + "." + hhmm;
        System.out.println(versionNo);
        return versionNo ;

    }
}

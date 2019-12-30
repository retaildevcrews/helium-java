package com.microsoft.azure.helium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class BuildConfig {

    @Autowired
    BuildProperties buildProperties;

    public String getBuildVersion(){

        String buildName = buildProperties.getName();
        String buildVersion = buildProperties.getVersion();
        String buildTime =  String.valueOf(buildProperties.getTime().getEpochSecond());
        System.out.println("buildName " + buildName +"_"+ "buildVersion "+ buildVersion + "_" + "buildTime "+ buildTime );
        return buildVersion+"."+buildTime ;
    }
}

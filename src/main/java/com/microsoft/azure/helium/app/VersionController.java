package com.microsoft.azure.helium.app;

import com.microsoft.azure.helium.Application;
import com.microsoft.azure.helium.config.BuildConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


@RestController
public class VersionController {
    @Autowired
    BuildConfig buildConfig;

    @GetMapping("/version")
    public String version() throws IOException {
        return buildConfig.getBuildVersion();
    }

}

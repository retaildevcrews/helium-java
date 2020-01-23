package com.microsoft.azure.helium.app;

import com.microsoft.azure.helium.config.BuildConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class VersionController {
    @Autowired
    BuildConfig buildConfig;

    @GetMapping("/version")
    @Cacheable(value = "version")
    public String version() throws IOException {
        return buildConfig.getBuildVersion();
    }

}

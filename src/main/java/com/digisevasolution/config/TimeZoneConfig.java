package com.digisevasolution.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        // Enforce JVM default timezone to UTC across all environments
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}

package com.digisevasolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigiSevaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigiSevaApplication.class, args);
        System.out.println("Digi Seva Solution Application Start............");
    }
}

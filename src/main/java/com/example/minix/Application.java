package com.example.minix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main application entry point.
 * Bootstraps the Spring Boot application.
 */
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package com.example.minix.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "auth-filter")
public record AuthFilterProperties(List<String> excludedPaths) {

    public AuthFilterProperties {
        excludedPaths = List.copyOf(excludedPaths);
    }
}
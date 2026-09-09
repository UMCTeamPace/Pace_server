package com.example.pace.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openweather")
public class OpenWeatherProperties {
    private String apiKey;
    private String currentWeatherUrl;
}

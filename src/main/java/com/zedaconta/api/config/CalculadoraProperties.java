package com.zedaconta.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "calculadora")
public class CalculadoraProperties {
    private String frontendUrl;
}

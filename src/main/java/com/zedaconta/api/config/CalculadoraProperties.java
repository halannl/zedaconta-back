package com.zedaconta.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.calculadora")
public class CalculadoraProperties {
    private String frontendUrl;
    private Integer maxRequisicoesSemLogin = 10; // Valor padrão caso não seja especificado
}

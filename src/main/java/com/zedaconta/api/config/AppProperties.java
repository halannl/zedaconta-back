package com.zedaconta.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para registrar as propriedades personalizadas da aplicação
 */
@Configuration
@EnableConfigurationProperties
public class AppProperties {
    
    /**
     * Configurações de JWT
     */
    @Configuration
    @ConfigurationProperties(prefix = "app.jwt")
    public static class JwtProperties {
        private String secret;
        private long expiration;
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
        
        public long getExpiration() {
            return expiration;
        }
        
        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }
    }
    
    /**
     * Configurações de CORS
     */
    @Configuration
    @ConfigurationProperties(prefix = "app.cors")
    public static class CorsProperties {
        private String allowedOrigins;
        private String allowedMethods;
        private String allowedHeaders;
        private String exposedHeaders;
        private boolean allowCredentials;
        private long maxAge;
        
        public String getAllowedOrigins() {
            return allowedOrigins;
        }
        
        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
        
        public String getAllowedMethods() {
            return allowedMethods;
        }
        
        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }
        
        public String getAllowedHeaders() {
            return allowedHeaders;
        }
        
        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }
        
        public String getExposedHeaders() {
            return exposedHeaders;
        }
        
        public void setExposedHeaders(String exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }
        
        public boolean isAllowCredentials() {
            return allowCredentials;
        }
        
        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
        
        public long getMaxAge() {
            return maxAge;
        }
        
        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }
}

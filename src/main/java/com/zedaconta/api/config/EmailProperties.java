package com.zedaconta.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configurações para o serviço de e-mail.
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {
    
    /**
     * Endereço de e-mail remetente.
     */
    private String from;
    
    /**
     * Assunto do e-mail de verificação.
     */
    private String verificationSubject;
    
    /**
     * Tempo de expiração do código de verificação em minutos.
     */
    private int verificationExpiryMinutes;
}

package com.zedaconta.api.dto;

import com.zedaconta.api.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para requisição de verificação de e-mail.
 */
@Data
public class EmailVerificationRequest {
    
    @NotBlank(message = "O e-mail não pode estar em branco")
    @ValidEmail
    private String email;
    
    @NotBlank(message = "O código de verificação não pode estar em branco")
    @Size(min = 6, max = 6, message = "O código de verificação deve ter 6 dígitos")
    private String code;
}

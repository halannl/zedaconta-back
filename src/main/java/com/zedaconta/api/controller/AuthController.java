package com.zedaconta.api.controller;

import com.zedaconta.api.dto.EmailVerificationRequest;
import com.zedaconta.api.model.User;
import com.zedaconta.api.security.JwtTokenProvider;
import com.zedaconta.api.service.UserService;
import com.zedaconta.api.validation.StrongPassword;
import com.zedaconta.api.validation.ValidEmail;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Auth controller is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate user and validate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Buscar usuário para obter informações adicionais
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String jwt = tokenProvider.createToken(user.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("tokenType", "Bearer");
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        // O papel INVALID_USER será atribuído no UserService
        
        User createdUser = userService.createUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso. Por favor, verifique seu e-mail para ativar sua conta.");
        response.put("userId", createdUser.getId());
        response.put("email", createdUser.getEmail());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint para verificação de e-mail.
     * Recebe o e-mail e o código de verificação enviado por e-mail.
     * 
     * @param request Requisição contendo e-mail e código de verificação
     * @return Resposta indicando sucesso ou falha na verificação
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
        log.debug("Recebida requisição de verificação de e-mail para: {}", request.getEmail());
        
        boolean verified = userService.verifyEmail(request.getEmail(), request.getCode());
        
        Map<String, Object> response = new HashMap<>();
        
        if (verified) {
            response.put("message", "E-mail verificado com sucesso. Agora você pode fazer login.");
            response.put("verified", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Falha na verificação de e-mail. Código inválido ou expirado.");
            response.put("verified", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "O e-mail não pode estar em branco")
        @ValidEmail
        private String email;

        @NotBlank(message = "A senha não pode estar em branco")
        private String password;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "O e-mail não pode estar em branco")
        @Size(max = 100, message = "O e-mail não pode ter mais de 100 caracteres")
        @ValidEmail
        private String email;

        @NotBlank(message = "A senha não pode estar em branco")
        @StrongPassword
        private String password;

        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 50, message = "O nome não pode ter mais de 50 caracteres")
        private String firstName;

        @NotBlank(message = "O sobrenome não pode estar em branco")
        @Size(max = 50, message = "O sobrenome não pode ter mais de 50 caracteres")
        private String lastName;
    }
}

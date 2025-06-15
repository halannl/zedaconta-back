package com.zedaconta.api.service;

import com.zedaconta.api.exception.UserAlreadyExistsException;
import com.zedaconta.api.model.Role;
import com.zedaconta.api.model.User;
import com.zedaconta.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final Random random = new Random();
    
    /**
     * Cria um novo usuário no sistema com status de e-mail não verificado.
     * Gera um código de verificação e envia por e-mail.
     * 
     * @param user O usuário a ser criado
     * @return O usuário criado com ID gerado
     * @throws UserAlreadyExistsException Se o e-mail já estiver em uso
     */
    public User createUser(User user) {
        log.debug("Tentativa de criação de usuário com e-mail: {}", user.getEmail());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Tentativa de criar usuário com e-mail já em uso: {}", user.getEmail());
            throw new UserAlreadyExistsException("E-mail já está em uso");
        }
        
        // Definir o papel inicial como INVALID_USER até que o e-mail seja verificado
        user.setRole(Role.INVALID_USER);
        user.setEmailVerified(false);
        
        // Gerar código de verificação
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(30)); // 30 minutos de validade
        
        // Codificar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Enviar e-mail de verificação
        emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationCode);
        
        log.info("Usuário criado com sucesso: {}", user.getEmail());
        
        return savedUser;
    }
    
    /**
     * Busca um usuário pelo ID.
     * 
     * @param id O ID do usuário a ser buscado
     * @return O usuário encontrado
     * @throws RuntimeException Se o usuário não for encontrado
     */
    public User getUserById(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com ID: {}", id);
                    return new RuntimeException("Usuário não encontrado com ID: " + id);
                });
    }
    
    /**
     * Busca um usuário pelo e-mail.
     * 
     * @param email O e-mail do usuário a ser buscado
     * @return O usuário encontrado ou null se não existir
     */
    public User getUserByEmail(String email) {
        log.debug("Buscando usuário por e-mail: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }
    
    /**
     * Busca um usuário pelo e-mail e retorna um Optional.
     * 
     * @param email O e-mail do usuário a ser buscado
     * @return Optional contendo o usuário, se encontrado
     */
    public java.util.Optional<User> findByEmail(String email) {
        log.debug("Buscando usuário por e-mail (Optional): {}", email);
        return userRepository.findByEmail(email);
    }
    
    /**
     * Verifica o código de verificação de e-mail.
     * 
     * @param email O e-mail do usuário
     * @param code O código de verificação
     * @return true se o código for válido, false caso contrário
     */
    public boolean verifyEmail(String email, String code) {
        log.debug("Tentativa de verificação de e-mail: {}", email);
        
        User user = getUserByEmail(email);
        if (user == null) {
            log.warn("Tentativa de verificação para e-mail inexistente: {}", email);
            return false;
        }
        
        // Verificar se o código já expirou
        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Código de verificação expirado para o e-mail: {}", email);
            return false;
        }
        
        // Verificar se o código está correto
        if (!user.getVerificationCode().equals(code)) {
            log.warn("Código de verificação inválido para o e-mail: {}", email);
            return false;
        }
        
        // Atualizar o usuário para verificado
        user.setEmailVerified(true);
        user.setRole(Role.USER); // Mudar de INVALID_USER para USER
        user.setVerificationCode(null); // Limpar o código após uso
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);
        
        log.info("E-mail verificado com sucesso: {}", email);
        return true;
    }
    
    /**
     * Gera um código de verificação numérico de 6 dígitos.
     * 
     * @return O código de verificação
     */
    private String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000); // Gera um número entre 100000 e 999999
        return String.valueOf(code);
    }
}

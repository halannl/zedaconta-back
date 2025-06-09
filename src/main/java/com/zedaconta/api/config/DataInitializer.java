package com.zedaconta.api.config;

import com.zedaconta.api.model.Role;
import com.zedaconta.api.model.User;
import com.zedaconta.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@zedaconta.com");
            admin.setFullName("Administrador");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            
            userRepository.save(admin);
            log.info("Admin user created successfully");
        }
        
        // Create regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@zedaconta.com");
            user.setFullName("Usuário Padrão");
            user.setRole(Role.USER);
            user.setEnabled(true);
            
            userRepository.save(user);
            log.info("Regular user created successfully");
        }
    }
}

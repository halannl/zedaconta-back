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
        if (!userRepository.existsByEmail("admin@zedaconta.com")) {
            User admin = new User();
            admin.setEmail("admin@zedaconta.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("Zedaconta");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setEmailVerified(true);
            
            userRepository.save(admin);
            log.info("Admin user created successfully");
        }
        
        // Create regular user if not exists
        if (!userRepository.existsByEmail("user@zedaconta.com")) {
            User user = new User();
            user.setEmail("user@zedaconta.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFirstName("Usuário");
            user.setLastName("Padrão");
            user.setRole(Role.USER);
            user.setEnabled(true);
            user.setEmailVerified(true);
            
            userRepository.save(user);
            log.info("Regular user created successfully");
        }
        
        // Create frontend user if not exists
        if (!userRepository.existsByEmail("frontend@zedaconta.com")) {
            User frontendUser = new User();
            frontendUser.setEmail("frontend@zedaconta.com");
            frontendUser.setPassword(passwordEncoder.encode("frontend123"));
            frontendUser.setFirstName("Usuário");
            frontendUser.setLastName("Frontend");
            frontendUser.setRole(Role.FRONTEND);
            frontendUser.setEnabled(true);
            frontendUser.setEmailVerified(true);
            
            userRepository.save(frontendUser);
            log.info("Frontend user created successfully");
        }
    }
}

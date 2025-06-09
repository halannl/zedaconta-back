package com.zedaconta.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ExampleController {

    @GetMapping("/public/info")
    public ResponseEntity<?> getPublicInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Esta é uma informação pública");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Informações do perfil do usuário");
        response.put("requiresAuth", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estatísticas administrativas");
        response.put("adminOnly", true);
        
        return ResponseEntity.ok(response);
    }
}

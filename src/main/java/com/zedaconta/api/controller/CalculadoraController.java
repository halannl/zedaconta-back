package com.zedaconta.api.controller;

import com.zedaconta.api.dto.CalculadoraJurosRequest;
import com.zedaconta.api.dto.CalculadoraJurosResponse;
import com.zedaconta.api.service.CalculadoraJurosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculadora")
@CrossOrigin(origins = "${calculadora.frontend-url}", allowCredentials = "true")
public class CalculadoraController {

    private final CalculadoraJurosService calculadoraJurosService;

    @PostMapping("/juros-compostos")
    @PreAuthorize("hasRole('FRONTEND') or hasRole('ADMIN')")
    public ResponseEntity<CalculadoraJurosResponse> calcularJurosCompostos(
            @Valid @RequestBody CalculadoraJurosRequest request) {
        
        CalculadoraJurosResponse response = calculadoraJurosService.calcularJurosCompostos(request);
        return ResponseEntity.ok(response);
    }
}

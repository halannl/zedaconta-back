package com.zedaconta.api.controller;

import com.zedaconta.api.dto.CalculadoraJurosRequest;
import com.zedaconta.api.dto.CalculadoraJurosResponse;
import com.zedaconta.api.service.CalculadoraJurosService;
import com.zedaconta.api.service.LimiteRequisicaoService;
import com.zedaconta.api.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculadora")
@CrossOrigin(origins = "${app.calculadora.frontend-url}", allowCredentials = "true")
public class CalculadoraController {

    private final CalculadoraJurosService calculadoraJurosService;
    private final LimiteRequisicaoService limiteRequisicaoService;

    @PostMapping("/juros-compostos")
    @PreAuthorize("hasRole('FRONTEND') or hasRole('ADMIN')")
    public ResponseEntity<CalculadoraJurosResponse> calcularJurosCompostos(
            @Valid @RequestBody CalculadoraJurosRequest request,
            HttpServletRequest httpRequest) {
        
        CalculadoraJurosResponse response = calculadoraJurosService.calcularJurosCompostos(request);
        
        // Verificar se o usuário está autenticado ou se está usando o acesso sem login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && "frontend-client".equals(authentication.getPrincipal())) {
            // Usuário não autenticado, adicionar informações sobre o limite de requisições
            String clientIp = IpUtils.obterIpCliente(httpRequest);
            int requisicoesRestantes = limiteRequisicaoService.requisicoesRestantes(clientIp);
            
            return ResponseEntity.ok()
                    .header("X-RemainingRequests", String.valueOf(requisicoesRestantes))
                    .body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    

}

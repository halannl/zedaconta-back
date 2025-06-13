package com.zedaconta.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zedaconta.api.service.LimiteRequisicaoService;
import com.zedaconta.api.util.IpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * Filtro de segurança específico para o endpoint da calculadora.
 * Permite acesso sem autenticação quando a requisição vier do host configurado,
 * limitando o número de requisições por IP conforme configuração.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CalculadoraSecurityFilter extends OncePerRequestFilter {

    private final LimiteRequisicaoService limiteRequisicaoService;
    private final ObjectMapper objectMapper;
    
    @Value("${app.calculadora.frontend-url}")
    private String calculadoraFrontendUrl;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String origin = request.getHeader("Origin");
        String path = request.getRequestURI();
        
        // Verificar se a requisição é para o endpoint da calculadora e vem do frontend configurado
        if (path != null && path.contains("/api/calculadora/") && origin != null && origin.equals(calculadoraFrontendUrl)) {
            // Verificar se já existe autenticação (usuário já está logado)
            if (SecurityContextHolder.getContext().getAuthentication() != null && 
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                
                // Usuário já está autenticado, permitir acesso normalmente
                log.debug("Usuário já autenticado, permitindo acesso ao endpoint da calculadora");
                
            } else {
                // Usuário não está autenticado, verificar limite de requisições
                String clientIp = IpUtils.obterIpCliente(request);
                
                if (limiteRequisicaoService.podeAcessar(clientIp)) {
                    // Ainda não atingiu o limite, registrar requisição e permitir acesso
                    limiteRequisicaoService.registrarRequisicao(clientIp);
                    
                    log.debug("Permitindo acesso não autenticado ao endpoint da calculadora. IP: {}, Requisições restantes: {}", 
                            clientIp, limiteRequisicaoService.requisicoesRestantes(clientIp));
                    
                    // Adicionar header com o número de requisições restantes
                    response.addHeader("X-RemainingRequests", String.valueOf(limiteRequisicaoService.requisicoesRestantes(clientIp)));
                    
                    // Conceder acesso com a role FRONTEND
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            "frontend-client", null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_FRONTEND"))
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Limite atingido, retornar erro 429 (Too Many Requests)
                    log.info("Limite de requisições atingido para o IP: {}", clientIp);
                    
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
                    responseBody.put("error", "Too Many Requests");
                    responseBody.put("message", "Limite de requisições não autenticadas atingido. Por favor, faça login para continuar.");
                    
                    objectMapper.writeValue(response.getOutputStream(), responseBody);
                    return; // Interromper o processamento do filtro
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    

}

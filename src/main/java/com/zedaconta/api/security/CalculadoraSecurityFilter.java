package com.zedaconta.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de segurança específico para o endpoint da calculadora.
 * Permite acesso sem autenticação quando a requisição vier do host configurado.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CalculadoraSecurityFilter extends OncePerRequestFilter {

    @Value("${calculadora.frontend-url}")
    private String calculadoraFrontendUrl;

    @Override
    protected void doFilterInternal(
            @lombok.NonNull HttpServletRequest request, 
            @lombok.NonNull HttpServletResponse response, 
            @lombok.NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String origin = request.getHeader("Origin");
        String path = request.getRequestURI();
        
        // Verificar se a requisição é para o endpoint da calculadora e vem do frontend configurado
        if (path != null && path.contains("/api/calculadora/") && origin != null && origin.equals(calculadoraFrontendUrl)) {
            log.debug("Permitindo acesso ao endpoint da calculadora a partir do frontend: {}", origin);
            
            // Conceder acesso com a role FRONTEND
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "frontend-client", null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_FRONTEND"))
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}

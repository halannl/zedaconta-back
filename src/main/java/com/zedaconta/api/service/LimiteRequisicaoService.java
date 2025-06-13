package com.zedaconta.api.service;

import com.zedaconta.api.config.CalculadoraProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço para controlar o limite de requisições não autenticadas por IP
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LimiteRequisicaoService {

    private final CalculadoraProperties calculadoraProperties;
    
    // Mapa para armazenar o contador de requisições por IP
    private final Map<String, Integer> contadorRequisicoesPorIp = new ConcurrentHashMap<>();
    
    /**
     * Registra uma requisição para o IP especificado e verifica se o limite foi atingido
     * 
     * @param ip O endereço IP do cliente
     * @return true se o IP ainda pode fazer requisições, false se o limite foi atingido
     */
    public boolean registrarRequisicao(String ip) {
        // Obter o limite configurado
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        
        // Obter o contador atual para o IP ou inicializar com 0
        int contadorAtual = contadorRequisicoesPorIp.getOrDefault(ip, 0);
        
        // Verificar se o limite já foi atingido
        if (contadorAtual >= limiteRequisicoes) {
            log.info("Limite de requisições sem login atingido para o IP: {}", ip);
            return false;
        }
        
        // Incrementar o contador
        contadorRequisicoesPorIp.put(ip, contadorAtual + 1);
        log.debug("Requisição registrada para o IP: {}. Total: {}/{}", ip, contadorAtual + 1, limiteRequisicoes);
        
        return true;
    }
    
    /**
     * Verifica se o IP ainda pode fazer requisições sem login
     * 
     * @param ip O endereço IP do cliente
     * @return true se o IP ainda pode fazer requisições, false se o limite foi atingido
     */
    public boolean podeAcessar(String ip) {
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        int contadorAtual = contadorRequisicoesPorIp.getOrDefault(ip, 0);
        
        return contadorAtual < limiteRequisicoes;
    }
    
    /**
     * Retorna o número de requisições restantes para o IP
     * 
     * @param ip O endereço IP do cliente
     * @return O número de requisições restantes
     */
    public int requisicoesRestantes(String ip) {
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        int contadorAtual = contadorRequisicoesPorIp.getOrDefault(ip, 0);
        
        return Math.max(0, limiteRequisicoes - contadorAtual);
    }
}

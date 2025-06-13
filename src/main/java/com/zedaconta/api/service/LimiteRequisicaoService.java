package com.zedaconta.api.service;

import com.zedaconta.api.config.CalculadoraProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço para controlar o limite de requisições não autenticadas por IP
 * O limite é compartilhado entre todas as calculadoras e é resetado diariamente às 00:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LimiteRequisicaoService {

    private final CalculadoraProperties calculadoraProperties;
    
    // Classe para armazenar o contador de requisições e a data
    private static class ContadorRequisicoes {
        private int contador;
        private LocalDate data;
        
        public ContadorRequisicoes(int contador, LocalDate data) {
            this.contador = contador;
            this.data = data;
        }
    }
    
    // Mapa para armazenar o contador de requisições por IP
    private final Map<String, ContadorRequisicoes> contadorRequisicoesPorIp = new ConcurrentHashMap<>();
    
    /**
     * Agenda o reset diário do contador de requisições às 00:00
     */
    @Scheduled(cron = "0 0 0 * * *") // Executa todos os dias às 00:00
    public void resetarContadores() {
        log.info("Resetando contadores de requisições não autenticadas");
        contadorRequisicoesPorIp.clear();
    }
    
    /**
     * Registra uma requisição para o IP especificado e verifica se o limite foi atingido
     * Verifica também se a data atual é diferente da data armazenada, resetando o contador se necessário
     * 
     * @param ip O endereço IP do cliente
     * @return true se o IP ainda pode fazer requisições, false se o limite foi atingido
     */
    public boolean registrarRequisicao(String ip) {
        // Obter o limite configurado
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        LocalDate hoje = LocalDate.now();
        
        // Obter o contador atual para o IP ou inicializar com 0
        ContadorRequisicoes contador = contadorRequisicoesPorIp.get(ip);
        
        // Se o contador não existe ou a data é diferente, inicializa com 0
        if (contador == null || !hoje.equals(contador.data)) {
            contador = new ContadorRequisicoes(0, hoje);
        }
        
        // Verificar se o limite já foi atingido
        if (contador.contador >= limiteRequisicoes) {
            log.info("Limite de requisições sem login atingido para o IP: {}", ip);
            return false;
        }
        
        // Incrementar o contador
        contador.contador++;
        contadorRequisicoesPorIp.put(ip, contador);
        log.debug("Requisição registrada para o IP: {}. Total: {}/{}", ip, contador.contador, limiteRequisicoes);
        
        return true;
    }
    
    /**
     * Verifica se o IP ainda pode fazer requisições sem login
     * Verifica também se a data atual é diferente da data armazenada, resetando o contador se necessário
     * 
     * @param ip O endereço IP do cliente
     * @return true se o IP ainda pode fazer requisições, false se o limite foi atingido
     */
    public boolean podeAcessar(String ip) {
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        LocalDate hoje = LocalDate.now();
        
        ContadorRequisicoes contador = contadorRequisicoesPorIp.get(ip);
        
        // Se o contador não existe ou a data é diferente, o cliente pode acessar
        if (contador == null || !hoje.equals(contador.data)) {
            return true;
        }
        
        return contador.contador < limiteRequisicoes;
    }
    
    /**
     * Retorna o número de requisições restantes para o IP
     * Verifica também se a data atual é diferente da data armazenada, resetando o contador se necessário
     * 
     * @param ip O endereço IP do cliente
     * @return O número de requisições restantes
     */
    public int requisicoesRestantes(String ip) {
        int limiteRequisicoes = calculadoraProperties.getMaxRequisicoesSemLogin();
        LocalDate hoje = LocalDate.now();
        
        ContadorRequisicoes contador = contadorRequisicoesPorIp.get(ip);
        
        // Se o contador não existe ou a data é diferente, o cliente tem todas as requisições disponíveis
        if (contador == null || !hoje.equals(contador.data)) {
            return limiteRequisicoes;
        }
        
        return Math.max(0, limiteRequisicoes - contador.contador);
    }
}

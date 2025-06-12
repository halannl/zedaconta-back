package com.zedaconta.api.service;

import com.zedaconta.api.dto.CalculadoraJurosRequest;
import com.zedaconta.api.dto.CalculadoraJurosResponse;
import com.zedaconta.api.dto.ResultadoMensalDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculadoraJurosService {

    public CalculadoraJurosResponse calcularJurosCompostos(CalculadoraJurosRequest request) {
        // Normalizar taxa de juros para o período mensal se necessário
        double taxaJurosMensal = normalizarTaxaJuros(request.getTaxaJuros(), request.getPeriodoJuros()); // Converter de porcentagem para decimal
        
        // Normalizar o tempo de investimento para meses
        int tempoEmMeses = normalizarTempoInvestimento(request.getTempoInvestimento(), request.getPeriodoInvestimento());
        
        // Cálculo de juros compostos com aportes mensais
        double valorInicial = request.getValorInicial();
        double aporteMensal = request.getValorMensal();
        
        // Lista para armazenar os resultados mensais
        List<ResultadoMensalDTO> resultadosMensais = new ArrayList<>();
        
        // Valores iniciais
        double montante = valorInicial;
        double totalInvestido = valorInicial;
        double totalJuros = 0.0;
        
        // Adicionar o mês 0 (situação inicial)
        resultadosMensais.add(ResultadoMensalDTO.builder()
                .mes(0)
                .valorInvestido(arredondar(valorInicial))
                .juros(0.0)
                .totalJuros(0.0)
                .total(arredondar(valorInicial))
                .build());
        
        // Calcular o montante mês a mês
        for (int mes = 1; mes <= tempoEmMeses; mes++) {
            // Calcular juros do mês
            double jurosMes = montante * taxaJurosMensal;
            
            // Atualizar o montante com os juros
            montante += jurosMes;
            
            // Adicionar o aporte mensal
            montante += aporteMensal;
            
            // Atualizar o total investido
            totalInvestido += aporteMensal;
            
            // Atualizar o total de juros
            totalJuros += jurosMes;
            
            // Adicionar o resultado do mês à lista
            resultadosMensais.add(ResultadoMensalDTO.builder()
                    .mes(mes)
                    .valorInvestido(arredondar(totalInvestido))
                    .juros(arredondar(jurosMes))
                    .totalJuros(arredondar(totalJuros))
                    .total(arredondar(montante))
                    .build());
        }
        
        return CalculadoraJurosResponse.builder()
                .valorFinal(arredondar(montante))
                .juros(arredondar(totalJuros))
                .totalInvestido(arredondar(totalInvestido))
                .resultadosMensais(resultadosMensais)
                .build();
    }
    
    /**
     * Arredonda um valor para duas casas decimais
     */
    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
    
    private double normalizarTaxaJuros(double taxa, String periodo) {
        switch (periodo.toLowerCase()) {
            case "anual":
                return Math.pow(1 + taxa, 1.0/12) - 1; // Converter taxa anual para mensal
            case "diaria":
                return Math.pow(1 + taxa, 30) - 1; // Converter taxa diária para mensal (aproximadamente)
            case "mensal":
            default:
                return taxa; // Taxa já está em base mensal
        }
    }
    
    private int normalizarTempoInvestimento(int tempo, String periodo) {
        switch (periodo.toLowerCase()) {
            case "ano":
            case "anos":
                return tempo * 12; // Converter anos para meses
            case "dia":
            case "dias":
                return tempo / 30; // Converter dias para meses (aproximadamente)
            case "mes":
            case "meses":
            default:
                return tempo; // Tempo já está em meses
        }
    }
}

package com.zedaconta.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CalculadoraJurosRequest {
    
    @NotNull
    @Positive
    private Double valorInicial;
    
    @NotNull
    @Positive
    private Double valorMensal;
    
    @NotNull
    @Positive
    private Double taxaJuros;
    
    @NotNull
    private String periodoJuros;
    
    @NotNull
    @Positive
    private Integer tempoInvestimento;
    
    @NotNull
    private String periodoInvestimento;
}

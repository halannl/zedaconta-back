package com.zedaconta.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculadoraJurosResponse {
    
    private Double valorFinal;
    private Double juros;
    private Double totalInvestido;
    private List<ResultadoMensalDTO> resultadosMensais;
}

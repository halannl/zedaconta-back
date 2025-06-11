package com.zedaconta.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoMensalDTO {
    private Integer mes;
    private Double valorInvestido;
    private Double juros;
    private Double totalJuros;
    private Double total;
}

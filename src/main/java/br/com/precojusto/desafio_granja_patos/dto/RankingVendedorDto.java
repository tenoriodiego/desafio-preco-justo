package br.com.precojusto.desafio_granja_patos.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingVendedorDto {
    private Long vendedorId;
    private String vendedorNome;
    private Long totalVendas;
    private BigDecimal totalValor;
}

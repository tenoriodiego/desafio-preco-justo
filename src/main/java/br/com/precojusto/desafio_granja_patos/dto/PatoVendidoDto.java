package br.com.precojusto.desafio_granja_patos.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatoVendidoDto {
    private Long id;
    private String nome;
    private OffsetDateTime dataVenda;
    private BigDecimal valorVenda;
    private String clienteNome;
    private String vendedorNome;
}

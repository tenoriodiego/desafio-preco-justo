package br.com.precojusto.desafio_granja_patos.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendaResponse {

    private Long id;
    private Long clienteId;
    private Long vendedorId;
    private OffsetDateTime dataVenda;
    private BigDecimal valorTotal;
    private BigDecimal descontoAplicado;
    private List<Long> patoIds;

}

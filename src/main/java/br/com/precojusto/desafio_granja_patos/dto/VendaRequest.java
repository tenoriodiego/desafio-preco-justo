package br.com.precojusto.desafio_granja_patos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendaRequest {

    private Long clienteId;
    private Long vendedorId;
    private List<Long> patoIds;

}

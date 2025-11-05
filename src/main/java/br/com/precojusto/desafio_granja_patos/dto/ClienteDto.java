package br.com.precojusto.desafio_granja_patos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDto {

    private Long id;
    private String nome;
    private boolean elegivelDesconto;
    private String contato;

}

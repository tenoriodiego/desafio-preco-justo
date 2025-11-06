package br.com.precojusto.desafio_granja_patos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeracaoParentalDto {

    private Long id;
    private String nome;
    private String descricao;

}

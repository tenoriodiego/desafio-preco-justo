package br.com.precojusto.desafio_granja_patos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatoDto {
    private Long id;
    private String nome;
    private Long maeId;
    private Integer filhoCount;
    private boolean vendido;
}

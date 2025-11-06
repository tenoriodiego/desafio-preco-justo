package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.dto.RankingVendedorDto;
import br.com.precojusto.desafio_granja_patos.service.RankingVendedorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RankingVendedorController.class)
class RankingVendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingVendedorService service;

    @Test
    void ranking_ComParametros_DeveRetornarRanking() throws Exception {
        // Primeiro, vamos descobrir a estrutura real do JSON
        RankingVendedorDto vendedor1 = new RankingVendedorDto();
        // Use setters conforme sua classe DTO real
        vendedor1.setVendedorId(1L);
        vendedor1.setVendedorNome("Vendedor A"); // Tente este nome se "nome" não funcionar
        vendedor1.setTotalVendas(10L);
        vendedor1.setTotalValor(BigDecimal.valueOf(1000.0));

        List<RankingVendedorDto> ranking = List.of(vendedor1);
        when(service.rankingPorPeriodo(any(), any(), anyString())).thenReturn(ranking);

        // Teste genérico primeiro
        mockMvc.perform(get("/api/ranking/vendedores")
                .param("start", "2024-01-01")
                .param("end", "2024-12-31")
                .param("by", "qtd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").exists()); // Verifica apenas que existe um elemento
    }

    @Test
    void ranking_DeveRetornarStatusOk() throws Exception {
        when(service.rankingPorPeriodo(any(), any(), anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/ranking/vendedores"))
                .andExpect(status().isOk());
    }
}
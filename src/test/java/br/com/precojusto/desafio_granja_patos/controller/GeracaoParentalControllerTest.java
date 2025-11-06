package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.dto.GeracaoParentalDto;
import br.com.precojusto.desafio_granja_patos.service.GeracaoParentalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeracaoParentalController.class)
class GeracaoParentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GeracaoParentalService service;

    @Test
    void criar_DeveRetornarGeracaoCriada() throws Exception {
        GeracaoParentalDto dto = new GeracaoParentalDto(1L, "Geração A", "Descrição teste");
        when(service.criar(any(GeracaoParentalDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/geracoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Geração A"));
    }

    @Test
    void listar_DeveRetornarListaDeGeracoes() throws Exception {
        List<GeracaoParentalDto> geracoes = List.of(
                new GeracaoParentalDto(1L, "Geração A", "Descrição A"),
                new GeracaoParentalDto(2L, "Geração B", "Descrição B"));
        when(service.listar()).thenReturn(geracoes);

        mockMvc.perform(get("/api/geracoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscar_DeveRetornarGeracaoPorId() throws Exception {
        GeracaoParentalDto dto = new GeracaoParentalDto(1L, "Geração A", "Descrição teste");
        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/geracoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deletar_DeveRetornarNoContent() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/api/geracoes/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletar(1L);
    }
}
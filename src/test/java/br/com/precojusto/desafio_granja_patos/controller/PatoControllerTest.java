package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.dto.PatoDto;
import br.com.precojusto.desafio_granja_patos.dto.PatoVendidoDto;
import br.com.precojusto.desafio_granja_patos.service.PatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatoController.class)
class PatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatoService patoService;

    @Test
    void criar_DeveRetornarPatoCriado() throws Exception {
        PatoDto dto = new PatoDto(1L, "Pato Teste", 2L, 2, true);
        when(patoService.criar(any(PatoDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/patos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pato Teste"));
    }

    @Test
    void listarPatosVendidos_DeveRetornarLista() throws Exception {
        List<PatoVendidoDto> patosVendidos = List.of(
                new PatoVendidoDto(1L, "Pato Vendido 1", OffsetDateTime.now(),
                        BigDecimal.valueOf(100.0), "Cliente A", "Vendedor X"),
                new PatoVendidoDto(2L, "Pato Vendido 2", OffsetDateTime.now(),
                        BigDecimal.valueOf(150.0), "Cliente B", "Vendedor Y"));
        when(patoService.listarPatosVendidos()).thenReturn(patosVendidos);

        mockMvc.perform(get("/api/patos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscar_DeveRetornarPatoPorId() throws Exception {
        PatoDto dto = new PatoDto(1L, "Pato Teste", 1L, 1, true);
        when(patoService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/patos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void delete_DeveRetornarNoContent() throws Exception {
        doNothing().when(patoService).deleteIfNotVendido(1L);

        mockMvc.perform(delete("/api/patos/1"))
                .andExpect(status().isNoContent());

        verify(patoService, times(1)).deleteIfNotVendido(1L);
    }
}
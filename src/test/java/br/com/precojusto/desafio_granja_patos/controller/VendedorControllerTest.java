package br.com.precojusto.desafio_granja_patos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.precojusto.desafio_granja_patos.dto.VendedorDto;
import br.com.precojusto.desafio_granja_patos.service.VendedorService;

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VendedorService service;

    @Test
    void deveCriarVendedor() throws Exception {
        // DTO no formato correto: (id, cpf, matricula, nome)
        VendedorDto dto = new VendedorDto(1L, "12345678900", "123", "Carlos");

        Mockito.when(service.criar(Mockito.any(VendedorDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/vendedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Carlos"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.matricula").value("123"));
    }

    @Test
    void deveListarVendedores() throws Exception {
        List<VendedorDto> lista = List.of(
                new VendedorDto(1L, "98765432100", "456", "Ana"),
                new VendedorDto(2L, "12345678900", "789", "Carlos"));

        Mockito.when(service.listar()).thenReturn(lista);

        mockMvc.perform(get("/api/vendedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Ana"))
                .andExpect(jsonPath("$[0].cpf").value("98765432100"))
                .andExpect(jsonPath("$[0].matricula").value("456"))
                .andExpect(jsonPath("$[1].nome").value("Carlos"));
    }

    @Test
    void deveDeletarVendedor() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/vendedores/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deletar(id);
    }
}

package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.dto.ClienteDto;
import br.com.precojusto.desafio_granja_patos.service.ClienteService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void criar_DeveRetornarClienteCriado() throws Exception {
        ClienteDto clienteDto = new ClienteDto(1L, "João Silva", true, "joao@email.com");
        when(clienteService.criar(any(ClienteDto.class))).thenReturn(clienteDto);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void listar_DeveRetornarListaDeClientes() throws Exception {
        List<ClienteDto> clientes = List.of(
                new ClienteDto(1L, "João Silva", true, "joao@email.com"),
                new ClienteDto(2L, "Maria Santos", false, "maria@email.com"));
        when(clienteService.listar()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));
    }
}
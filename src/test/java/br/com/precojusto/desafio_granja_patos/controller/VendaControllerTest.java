package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.dto.VendaRequest;
import br.com.precojusto.desafio_granja_patos.dto.VendaResponse;
import br.com.precojusto.desafio_granja_patos.service.VendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VendaService vendaService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveCriarVenda() throws Exception {
        VendaResponse resp = new VendaResponse();
        resp.setId(1L);
        Mockito.when(vendaService.criarVenda(Mockito.any())).thenReturn(resp);

        mockMvc.perform(post("/api/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new VendaRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveListarVendas() throws Exception {
        Mockito.when(vendaService.listarVendas())
                .thenReturn(List.of(new VendaResponse(1L, null, null, null, null, null, null)));

        mockMvc.perform(get("/api/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}

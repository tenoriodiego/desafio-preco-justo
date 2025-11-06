package br.com.precojusto.desafio_granja_patos.controller;

import br.com.precojusto.desafio_granja_patos.service.RelatorioVendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelatorioVendaController.class)
class RelatorioVendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelatorioVendaService relatorioVendaService;

    @Test
    void gerarRelatorioVendas_DeveRetornarArquivoExcel() throws Exception {
        byte[] excelContent = new byte[] { 1, 2, 3, 4, 5 };
        when(relatorioVendaService.gerarRelatorio(any(), any())).thenReturn(excelContent);

        mockMvc.perform(get("/relatorios/vendas/excel")
                .param("dataInicial", "2024-01-01")
                .param("dataFinal", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"relatorio_vendas.xlsx\""))
                .andExpect(content().bytes(excelContent));
    }

    @Test
    void gerarRelatorioVendas_SemParametros_DeveRetornarArquivoExcel() throws Exception {
        byte[] excelContent = new byte[] { 1, 2, 3, 4, 5 };
        when(relatorioVendaService.gerarRelatorio(any(), any())).thenReturn(excelContent);

        mockMvc.perform(get("/relatorios/vendas/excel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }
}
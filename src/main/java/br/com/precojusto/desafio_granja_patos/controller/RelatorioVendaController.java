package br.com.precojusto.desafio_granja_patos.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.precojusto.desafio_granja_patos.service.RelatorioVendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/relatorios/vendas")
@Tag(name = "Relatórios de Venda", description = "Geração de relatórios de venda em Excel")
public class RelatorioVendaController {

    private final RelatorioVendaService relatorioVendaService;

    public RelatorioVendaController(RelatorioVendaService relatorioVendaService) {
        this.relatorioVendaService = relatorioVendaService;
    }

    @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Gerar relatório de vendas em Excel (.xlsx)")
    public ResponseEntity<byte[]> gerarRelatorioVendas(
            @Parameter(description = "Data inicial (formato: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,

            @Parameter(description = "Data final (formato: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
            throws IOException {

        byte[] relatorio = relatorioVendaService.gerarRelatorio(dataInicial, dataFinal);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("relatorio_vendas.xlsx")
                        .build());

        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(relatorio);
    }
}

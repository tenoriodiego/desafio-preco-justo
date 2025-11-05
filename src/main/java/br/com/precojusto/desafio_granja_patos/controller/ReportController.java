package br.com.precojusto.desafio_granja_patos.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.precojusto.desafio_granja_patos.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/patos-cadastrados", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportPatos(HttpServletResponse response) throws Exception {
        reportService.exportPatosCadastrados(response);
    }
}

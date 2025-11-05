package br.com.precojusto.desafio_granja_patos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.precojusto.desafio_granja_patos.dto.VendaRequest;
import br.com.precojusto.desafio_granja_patos.dto.VendaResponse;
import br.com.precojusto.desafio_granja_patos.service.VendaService;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponse> criar(@RequestBody VendaRequest req) {
        return ResponseEntity.ok(vendaService.criarVenda(req));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> listar() {
        return ResponseEntity.ok(vendaService.listarVendas());
    }
}
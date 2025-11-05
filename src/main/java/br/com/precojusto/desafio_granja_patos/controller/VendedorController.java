package br.com.precojusto.desafio_granja_patos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.precojusto.desafio_granja_patos.dto.VendedorDto;
import br.com.precojusto.desafio_granja_patos.service.VendedorService;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {
    private final VendedorService vendedorService;

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @PostMapping
    public ResponseEntity<VendedorDto> criar(@RequestBody VendedorDto dto) {
        return ResponseEntity.ok(vendedorService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendedorDto>> listar() {
        return ResponseEntity.ok(vendedorService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
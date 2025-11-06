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

import br.com.precojusto.desafio_granja_patos.dto.GeracaoParentalDto;
import br.com.precojusto.desafio_granja_patos.service.GeracaoParentalService;

@RestController
@RequestMapping("/api/geracoes")
public class GeracaoParentalController {

    private final GeracaoParentalService service;

    public GeracaoParentalController(GeracaoParentalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GeracaoParentalDto> criar(@RequestBody GeracaoParentalDto dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<GeracaoParentalDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeracaoParentalDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
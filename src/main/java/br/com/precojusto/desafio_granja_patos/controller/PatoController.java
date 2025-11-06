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

import br.com.precojusto.desafio_granja_patos.dto.PatoDto;
import br.com.precojusto.desafio_granja_patos.dto.PatoVendidoDto;
import br.com.precojusto.desafio_granja_patos.service.PatoService;

@RestController
@RequestMapping("/api/patos")
public class PatoController {
    private final PatoService patoService;

    public PatoController(PatoService patoService) {
        this.patoService = patoService;
    }

    @PostMapping
    public ResponseEntity<PatoDto> criar(@RequestBody PatoDto dto) {
        return ResponseEntity.ok(patoService.criar(dto));
    }

    @GetMapping()
    public ResponseEntity<List<PatoVendidoDto>> listarPatosVendidos() {
        List<PatoVendidoDto> patosVendidos = patoService.listarPatosVendidos();
        return ResponseEntity.ok(patosVendidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatoDto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(patoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patoService.deleteIfNotVendido(id);
        return ResponseEntity.noContent().build();
    }
}
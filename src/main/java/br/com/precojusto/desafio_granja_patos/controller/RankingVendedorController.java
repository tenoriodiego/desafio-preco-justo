package br.com.precojusto.desafio_granja_patos.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.precojusto.desafio_granja_patos.dto.RankingVendedorDto;
import br.com.precojusto.desafio_granja_patos.service.RankingVendedorService;

@RestController
@RequestMapping("/api/ranking")
public class RankingVendedorController {

    private final RankingVendedorService service;

    public RankingVendedorController(RankingVendedorService service) {
        this.service = service;
    }

    @GetMapping("/vendedores")
    public List<RankingVendedorDto> ranking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false, defaultValue = "qtd") String by) {
        OffsetDateTime s = start == null ? OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                : start.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
        OffsetDateTime e = end == null ? OffsetDateTime.now()
                : end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toOffsetDateTime();
        return service.rankingPorPeriodo(s, e, by);
    }
}

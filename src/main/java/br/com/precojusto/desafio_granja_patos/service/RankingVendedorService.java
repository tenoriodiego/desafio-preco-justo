package br.com.precojusto.desafio_granja_patos.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.RankingVendedorDto;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendedorRepository;

@Service
public class RankingVendedorService {

    private final VendaRepository vendaRepository;
    private final VendedorRepository vendedorRepository;

    public RankingVendedorService(VendaRepository vendaRepository, VendedorRepository vendedorRepository) {
        this.vendaRepository = vendaRepository;
        this.vendedorRepository = vendedorRepository;
    }

    public List<RankingVendedorDto> rankingPorPeriodo(OffsetDateTime start, OffsetDateTime end, String orderBy) {
        List<Object[]> rows = vendaRepository.aggregateByVendedor(start, end);
        return rows.stream().map(r -> {
            Long vendedorId = ((Number) r[0]).longValue();
            Long totalVendas = ((Number) r[1]).longValue();
            BigDecimal totalValor = (BigDecimal) r[2];
            String nome = vendedorRepository.findById(vendedorId).map(v -> v.getNome()).orElse("-");
            RankingVendedorDto dto = new RankingVendedorDto();
            dto.setVendedorId(vendedorId);
            dto.setVendedorNome(nome);
            dto.setTotalVendas(totalVendas);
            dto.setTotalValor(totalValor == null ? BigDecimal.ZERO : totalValor);
            return dto;
        }).sorted((a, b) -> {
            if ("valor".equalsIgnoreCase(orderBy))
                return b.getTotalValor().compareTo(a.getTotalValor());
            return Long.compare(b.getTotalVendas(), a.getTotalVendas());
        }).collect(Collectors.toList());
    }
}

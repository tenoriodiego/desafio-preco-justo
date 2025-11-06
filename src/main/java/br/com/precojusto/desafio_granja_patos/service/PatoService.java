package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.PatoDto;
import br.com.precojusto.desafio_granja_patos.entity.Pato;
import br.com.precojusto.desafio_granja_patos.exception.NotFoundException;
import br.com.precojusto.desafio_granja_patos.repository.PatoRepository;
import jakarta.transaction.Transactional;

@Service
public class PatoService {
    private final PatoRepository patoRepository;

    public PatoService(PatoRepository patoRepository) {
        this.patoRepository = patoRepository;
    }

    @Transactional
    public PatoDto criar(PatoDto dto) {
        Pato p = new Pato();
        p.setNome(dto.getNome());
        p.setFilhoCount(dto.getFilhoCount());

        // Só tenta buscar mãe se o ID for informado e existir
        if (dto.getMaeId() != null) {
            patoRepository.findById(dto.getMaeId())
                    .ifPresentOrElse(
                            p::setMae,
                            () -> {
                                throw new NotFoundException("Mãe não encontrada");
                            });
        }

        p.setPrecoCateg(categFromFilhos(p.getFilhoCount()));
        Pato saved = patoRepository.save(p);

        return toDto(saved);
    }

    public List<PatoDto> listar(Boolean vendido) {
        return patoRepository.findAll().stream()
                .filter(p -> vendido == null || p.isVendido() == vendido)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PatoDto buscarPorId(Long id) {
        return patoRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Pato não encontrado"));
    }

    @Transactional
    public void deleteIfNotVendido(Long id) {
        Pato p = patoRepository.findById(id).orElseThrow(() -> new NotFoundException("Pato não encontrado"));
        if (p.isVendido())
            throw new IllegalStateException("Não é possível excluir pato vendido");
        patoRepository.delete(p);
    }

    private PatoDto toDto(Pato p) {
        PatoDto d = new PatoDto();
        d.setId(p.getId());
        d.setNome(p.getNome());
        d.setMaeId(p.getMae() == null ? null : p.getMae().getId());
        d.setFilhoCount(p.getFilhoCount());
        d.setVendido(p.isVendido());
        return d;
    }

    private String categFromFilhos(Integer f) {
        if (f == null)
            return "sem_filhos";
        return switch (f) {
            case 1 -> "1_filho";
            case 2 -> "2_filhos";
            default -> "sem_filhos";
        };
    }
}
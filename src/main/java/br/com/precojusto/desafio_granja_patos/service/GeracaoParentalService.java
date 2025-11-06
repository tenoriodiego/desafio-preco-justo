package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.GeracaoParentalDto;
import br.com.precojusto.desafio_granja_patos.entity.GeracaoParental;
import br.com.precojusto.desafio_granja_patos.exception.NotFoundException;
import br.com.precojusto.desafio_granja_patos.repository.GeracaoParentalRepository;
import jakarta.transaction.Transactional;

@Service
public class GeracaoParentalService {

    private final GeracaoParentalRepository repository;

    public GeracaoParentalService(GeracaoParentalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public GeracaoParentalDto criar(GeracaoParentalDto dto) {
        GeracaoParental entity = new GeracaoParental();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());

        GeracaoParental saved = repository.save(entity);
        dto.setId(saved.getId());
        return dto;
    }

    public List<GeracaoParentalDto> listar() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public GeracaoParentalDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Geração parental não encontrada"));
    }

    public void deletar(Long id) {
        GeracaoParental gp = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Geração parental não encontrada"));
        repository.delete(gp);
    }

    private GeracaoParentalDto toDto(GeracaoParental entity) {
        GeracaoParentalDto dto = new GeracaoParentalDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        return dto;
    }
}

package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.VendedorDto;
import br.com.precojusto.desafio_granja_patos.entity.Vendedor;
import br.com.precojusto.desafio_granja_patos.exception.NotFoundException;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendedorRepository;

@Service
public class VendedorService {
    private final VendedorRepository vendedorRepository;
    private final VendaRepository vendaRepository;

    public VendedorService(VendedorRepository vendedorRepository, VendaRepository vendaRepository) {
        this.vendedorRepository = vendedorRepository;
        this.vendaRepository = vendaRepository;
    }

    public VendedorDto criar(VendedorDto dto) {
        if (vendedorRepository.existsByCpf(dto.getCpf()))
            throw new IllegalArgumentException("CPF já cadastrado");
        if (vendedorRepository.existsByMatricula(dto.getMatricula()))
            throw new IllegalArgumentException("Matrícula já cadastrada");
        Vendedor v = new Vendedor();
        v.setCpf(dto.getCpf());
        v.setMatricula(dto.getMatricula());
        v.setNome(dto.getNome());
        Vendedor save = vendedorRepository.save(v);
        dto.setId(save.getId());
        return dto;
    }

    public List<VendedorDto> listar() {
        return vendedorRepository.findAll().stream().map(v -> {
            VendedorDto d = new VendedorDto();
            d.setId(v.getId());
            d.setCpf(v.getCpf());
            d.setMatricula(v.getMatricula());
            d.setNome(v.getNome());
            return d;
        }).collect(Collectors.toList());
    }

    public void deletar(Long id) {
        if (vendaRepository.existsByVendedorId(id))
            throw new IllegalStateException("Não é possível excluir vendedor com vendas");
        vendedorRepository.deleteById(id);
    }

    public Vendedor buscarEntidade(Long id) {
        return vendedorRepository.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado"));
    }
}
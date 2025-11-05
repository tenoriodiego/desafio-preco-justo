package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.ClienteDto;
import br.com.precojusto.desafio_granja_patos.entity.Cliente;
import br.com.precojusto.desafio_granja_patos.exception.NotFoundException;
import br.com.precojusto.desafio_granja_patos.repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDto criar(ClienteDto dto) {
        Cliente c = new Cliente();
        c.setNome(dto.getNome());
        c.setElegivelDesconto(dto.isElegivelDesconto());
        c.setContato(dto.getContato());
        Cliente saved = clienteRepository.save(c);
        dto.setId(saved.getId());
        return dto;
    }

    public List<ClienteDto> listar() {
        return clienteRepository.findAll().stream().map(c -> {
            ClienteDto d = new ClienteDto();
            d.setId(c.getId());
            d.setNome(c.getNome());
            d.setElegivelDesconto(c.isElegivelDesconto());
            d.setContato(c.getContato());
            return d;
        }).collect(Collectors.toList());
    }

    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }
}

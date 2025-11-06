package br.com.precojusto.desafio_granja_patos.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.precojusto.desafio_granja_patos.dto.VendaRequest;
import br.com.precojusto.desafio_granja_patos.dto.VendaResponse;
import br.com.precojusto.desafio_granja_patos.entity.Cliente;
import br.com.precojusto.desafio_granja_patos.entity.Pato;
import br.com.precojusto.desafio_granja_patos.entity.Venda;
import br.com.precojusto.desafio_granja_patos.entity.VendaPato;
import br.com.precojusto.desafio_granja_patos.entity.Vendedor;
import br.com.precojusto.desafio_granja_patos.exception.NotFoundException;
import br.com.precojusto.desafio_granja_patos.repository.PatoRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendaPatoRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import jakarta.transaction.Transactional;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final PatoRepository patoRepository;
    private final ClienteService clienteService;
    private final VendedorService vendedorService;
    private final VendaPatoRepository vendaPatoRepository;

    public VendaService(VendaRepository vendaRepository, PatoRepository patoRepository,
            ClienteService clienteService, VendedorService vendedorService,
            VendaPatoRepository vendaPatoRepository) {
        this.vendaRepository = vendaRepository;
        this.patoRepository = patoRepository;
        this.clienteService = clienteService;
        this.vendedorService = vendedorService;
        this.vendaPatoRepository = vendaPatoRepository;
    }

    private BigDecimal precificar(Pato p) {
        if (p.getFilhoCount() == null)
            return BigDecimal.valueOf(70);
        return switch (p.getFilhoCount()) {
            case 1 -> BigDecimal.valueOf(50);
            case 2 -> BigDecimal.valueOf(25);
            default -> BigDecimal.valueOf(70);
        };
    }

    @Transactional
    public VendaResponse criarVenda(VendaRequest req) {
        Cliente cliente = clienteService.buscarEntidade(req.getClienteId());
        Vendedor vendedor = vendedorService.buscarEntidade(req.getVendedorId());

        List<Pato> patos = patoRepository.findAllById(req.getPatoIds());
        if (patos.size() != req.getPatoIds().size())
            throw new NotFoundException("Algum pato não encontrado");
        if (patos.stream().anyMatch(Pato::isVendido))
            throw new IllegalStateException("Um ou mais patos já foram vendidos");

        BigDecimal total = patos.stream().map(this::precificar).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal desconto = cliente.isElegivelDesconto() ? total.multiply(BigDecimal.valueOf(0.20)) : BigDecimal.ZERO;
        BigDecimal totalComDesconto = total.subtract(desconto);

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setVendedor(vendedor);
        venda.setValorTotal(totalComDesconto);
        venda.setDescontoAplicado(desconto);
        Venda saved = vendaRepository.save(venda);

        for (Pato p : patos) {
            VendaPato vp = new VendaPato();
            VendaPato.VendaPatoId id = new VendaPato.VendaPatoId();
            id.setVendaId(saved.getId());
            id.setPatoId(p.getId());
            vp.setId(id);
            vp.setVenda(saved);
            vp.setPato(p);
            vp.setPrecoUnitario(precificar(p));
            vendaPatoRepository.save(vp);

            p.setVendido(true);
            patoRepository.save(p);
        }

        VendaResponse resp = new VendaResponse();
        resp.setId(saved.getId());
        resp.setClienteId(cliente.getId());
        resp.setVendedorId(vendedor.getId());
        resp.setDataVenda(saved.getDataVenda());
        resp.setValorTotal(saved.getValorTotal());
        resp.setDescontoAplicado(saved.getDescontoAplicado());
        resp.setPatoIds(req.getPatoIds().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return resp;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<VendaResponse> listarVendas() {
        return vendaRepository.findAllWithItens().stream().map(v -> {
            VendaResponse r = new VendaResponse();
            r.setId(v.getId());
            r.setClienteId(v.getCliente() != null ? v.getCliente().getId() : null);
            r.setVendedorId(v.getVendedor() != null ? v.getVendedor().getId() : null);
            r.setDataVenda(v.getDataVenda());
            r.setValorTotal(v.getValorTotal());
            r.setDescontoAplicado(v.getDescontoAplicado());
            r.setPatoIds(v.getItens() == null ? Collections.emptyList()
                    : v.getItens().stream()
                            .filter(i -> i.getPato() != null)
                            .map(i -> i.getPato().getId())
                            .collect(Collectors.toList()));
            return r;
        }).collect(Collectors.toList());
    }

}
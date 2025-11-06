package br.com.precojusto.desafio_granja_patos.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

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

class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private PatoRepository patoRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private VendedorService vendedorService;

    @Mock
    private VendaPatoRepository vendaPatoRepository;

    @InjectMocks
    private VendaService vendaService;

    private Cliente cliente;
    private Vendedor vendedor;
    private Pato pato1;
    private Pato pato2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setElegivelDesconto(true);

        vendedor = new Vendedor();
        vendedor.setId(2L);
        vendedor.setNome("Maria");

        pato1 = new Pato();
        pato1.setId(10L);
        pato1.setNome("Pato 1");
        pato1.setFilhoCount(1); // preço 50

        pato2 = new Pato();
        pato2.setId(11L);
        pato2.setNome("Pato 2");
        pato2.setFilhoCount(2); // preço 25
    }

    @Test
    void deveCriarVendaComDesconto() {
        // Mock do request
        VendaRequest req = new VendaRequest();
        req.setClienteId(1L);
        req.setVendedorId(2L);
        req.setPatoIds(List.of(10L, 11L));

        // Mock dos serviços e repositórios
        when(clienteService.buscarEntidade(1L)).thenReturn(cliente);
        when(vendedorService.buscarEntidade(2L)).thenReturn(vendedor);
        when(patoRepository.findAllById(List.of(10L, 11L))).thenReturn(List.of(pato1, pato2));

        Venda vendaSalva = new Venda();
        vendaSalva.setId(100L);
        vendaSalva.setCliente(cliente);
        vendaSalva.setVendedor(vendedor);
        vendaSalva.setValorTotal(BigDecimal.valueOf(60));
        vendaSalva.setDescontoAplicado(BigDecimal.valueOf(15));
        vendaSalva.setDataVenda(OffsetDateTime.now());

        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaSalva);

        // Execução
        VendaResponse resp = vendaService.criarVenda(req);

        // Verificações
        assertNotNull(resp);
        assertEquals(100L, resp.getId());
        assertEquals(1L, resp.getClienteId());
        assertEquals(2L, resp.getVendedorId());
        assertEquals(BigDecimal.valueOf(15), resp.getDescontoAplicado());
        assertEquals(BigDecimal.valueOf(60), resp.getValorTotal());
        assertEquals(2, resp.getPatoIds().size());

        // Verifica se pato foi marcado como vendido
        verify(patoRepository, times(2)).save(any(Pato.class));
        assertTrue(pato1.isVendido());
        assertTrue(pato2.isVendido());

        // Verifica se a venda foi salva
        verify(vendaRepository).save(any(Venda.class));

        // Verifica se VendaPato foi persistido 2x
        verify(vendaPatoRepository, times(2)).save(any(VendaPato.class));
    }

    @Test
    void deveLancarErroSeAlgumPatoNaoExistir() {
        VendaRequest req = new VendaRequest();
        req.setClienteId(1L);
        req.setVendedorId(2L);
        req.setPatoIds(List.of(10L, 11L));

        when(clienteService.buscarEntidade(1L)).thenReturn(cliente);
        when(vendedorService.buscarEntidade(2L)).thenReturn(vendedor);
        when(patoRepository.findAllById(List.of(10L, 11L))).thenReturn(List.of(pato1)); // falta um pato

        assertThrows(NotFoundException.class, () -> vendaService.criarVenda(req));
    }

    @Test
    void deveLancarErroSeAlgumPatoJaVendido() {
        pato1.setVendido(true);

        VendaRequest req = new VendaRequest();
        req.setClienteId(1L);
        req.setVendedorId(2L);
        req.setPatoIds(List.of(10L, 11L));

        when(clienteService.buscarEntidade(1L)).thenReturn(cliente);
        when(vendedorService.buscarEntidade(2L)).thenReturn(vendedor);
        when(patoRepository.findAllById(List.of(10L, 11L))).thenReturn(List.of(pato1, pato2));

        assertThrows(IllegalStateException.class, () -> vendaService.criarVenda(req));
    }
}

package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.precojusto.desafio_granja_patos.dto.ClienteDto;
import br.com.precojusto.desafio_granja_patos.entity.Cliente;
import br.com.precojusto.desafio_granja_patos.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void criar_DeveRetornarClienteDtoComId() {
        // Arrange
        ClienteDto dto = new ClienteDto();
        dto.setNome("João Silva");
        dto.setContato("joao@email.com");
        dto.setElegivelDesconto(true);

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("João Silva");
        clienteSalvo.setContato("joao@email.com");
        clienteSalvo.setElegivelDesconto(true);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        ClienteDto resultado = clienteService.criar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getContato());
        assertTrue(resultado.isElegivelDesconto());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void listar_DeveRetornarListaDeClientes() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Cliente 1");
        cliente1.setContato("contato1@email.com");
        cliente1.setElegivelDesconto(true);

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Cliente 2");
        cliente2.setContato("contato2@email.com");
        cliente2.setElegivelDesconto(false);

        when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2));

        // Act
        List<ClienteDto> resultado = clienteService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cliente 1", resultado.get(0).getNome());
        assertEquals("Cliente 2", resultado.get(1).getNome());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void buscarEntidade_QuandoClienteExiste_DeveRetornarCliente() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.buscarEntidade(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente Teste", resultado.getNome());
    }

    @Test
    void buscarEntidade_QuandoClienteNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clienteService.buscarEntidade(1L));
    }
}
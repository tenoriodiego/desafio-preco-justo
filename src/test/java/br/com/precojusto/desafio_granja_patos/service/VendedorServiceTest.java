package br.com.precojusto.desafio_granja_patos.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.precojusto.desafio_granja_patos.dto.VendedorDto;
import br.com.precojusto.desafio_granja_patos.entity.Vendedor;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendedorRepository;

@ExtendWith(MockitoExtension.class)
class VendedorServiceTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VendedorService vendedorService;

    @Test
    void criar_ComDadosValidos_DeveRetornarVendedorDto() {
        // Arrange
        VendedorDto dto = new VendedorDto();
        dto.setCpf("12345678901");
        dto.setMatricula("MAT001");
        dto.setNome("João Vendedor");

        Vendedor vendedorSalvo = new Vendedor();
        vendedorSalvo.setId(1L);
        vendedorSalvo.setCpf("12345678901");
        vendedorSalvo.setMatricula("MAT001");
        vendedorSalvo.setNome("João Vendedor");

        when(vendedorRepository.existsByCpf("12345678901")).thenReturn(false);
        when(vendedorRepository.existsByMatricula("MAT001")).thenReturn(false);
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorSalvo);

        // Act
        VendedorDto resultado = vendedorService.criar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Vendedor", resultado.getNome());
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }

    @Test
    void criar_ComCpfDuplicado_DeveLancarExcecao() {
        // Arrange
        VendedorDto dto = new VendedorDto();
        dto.setCpf("12345678901");
        dto.setMatricula("MAT001");
        dto.setNome("João Vendedor");

        when(vendedorRepository.existsByCpf("12345678901")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> vendedorService.criar(dto));
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }

    @Test
    void criar_ComMatriculaDuplicada_DeveLancarExcecao() {
        // Arrange
        VendedorDto dto = new VendedorDto();
        dto.setCpf("12345678901");
        dto.setMatricula("MAT001");
        dto.setNome("João Vendedor");

        when(vendedorRepository.existsByCpf("12345678901")).thenReturn(false);
        when(vendedorRepository.existsByMatricula("MAT001")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> vendedorService.criar(dto));
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }

    @Test
    void listar_DeveRetornarListaDeVendedores() {
        // Arrange
        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId(1L);
        vendedor1.setNome("Vendedor 1");
        vendedor1.setCpf("11111111111");
        vendedor1.setMatricula("MAT001");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId(2L);
        vendedor2.setNome("Vendedor 2");
        vendedor2.setCpf("22222222222");
        vendedor2.setMatricula("MAT002");

        when(vendedorRepository.findAll()).thenReturn(List.of(vendedor1, vendedor2));

        // Act
        List<VendedorDto> resultado = vendedorService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Vendedor 1", resultado.get(0).getNome());
        assertEquals("Vendedor 2", resultado.get(1).getNome());
    }

    @Test
    void deletar_QuandoSemVendas_DeveDeletarVendedor() {
        // Arrange
        when(vendaRepository.existsByVendedorId(1L)).thenReturn(false);
        doNothing().when(vendedorRepository).deleteById(1L);

        // Act
        vendedorService.deletar(1L);

        // Assert
        verify(vendedorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_QuandoComVendas_DeveLancarExcecao() {
        // Arrange
        when(vendaRepository.existsByVendedorId(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> vendedorService.deletar(1L));
        verify(vendedorRepository, never()).deleteById(any());
    }

    @Test
    void buscarEntidade_QuandoExiste_DeveRetornarVendedor() {
        // Arrange
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("Vendedor Teste");

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));

        // Act
        Vendedor resultado = vendedorService.buscarEntidade(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Vendedor Teste", resultado.getNome());
    }

    @Test
    void buscarEntidade_QuandoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> vendedorService.buscarEntidade(1L));
    }
}
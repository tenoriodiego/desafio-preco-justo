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

import br.com.precojusto.desafio_granja_patos.dto.PatoDto;
import br.com.precojusto.desafio_granja_patos.dto.PatoVendidoDto;
import br.com.precojusto.desafio_granja_patos.entity.Pato;
import br.com.precojusto.desafio_granja_patos.repository.PatoRepository;

@ExtendWith(MockitoExtension.class)
class PatoServiceTest {

    @Mock
    private PatoRepository patoRepository;

    @InjectMocks
    private PatoService patoService;

    @Test
    void criar_SemMae_DeveRetornarPatoDto() {
        // Arrange
        PatoDto dto = new PatoDto();
        dto.setNome("Pato Teste");
        dto.setFilhoCount(0);
        dto.setVendido(false);
        dto.setMaeId(null);

        Pato patoSalvo = new Pato();
        patoSalvo.setId(1L);
        patoSalvo.setNome("Pato Teste");
        patoSalvo.setFilhoCount(0);
        patoSalvo.setVendido(false);

        when(patoRepository.save(any(Pato.class))).thenReturn(patoSalvo);

        // Act
        PatoDto resultado = patoService.criar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pato Teste", resultado.getNome());

        verify(patoRepository, times(1)).save(any(Pato.class));
    }

    @Test
    void criar_ComMaeExistente_DeveRetornarPatoComMae() {
        // Arrange
        Pato mae = new Pato();
        mae.setId(99L);
        mae.setNome("Mãe Pato");

        PatoDto dto = new PatoDto();
        dto.setNome("Filho Pato");
        dto.setMaeId(99L);

        Pato patoSalvo = new Pato();
        patoSalvo.setId(1L);
        patoSalvo.setNome("Filho Pato");
        patoSalvo.setMae(mae);

        when(patoRepository.findById(99L)).thenReturn(Optional.of(mae));
        when(patoRepository.save(any(Pato.class))).thenReturn(patoSalvo);

        // Act
        PatoDto resultado = patoService.criar(dto);

        // Assert
        assertNotNull(resultado);
        verify(patoRepository, times(1)).findById(99L);
    }

    @Test
    void criar_ComMaeInexistente_DeveLancarExcecao() {
        // Arrange
        PatoDto dto = new PatoDto();
        dto.setNome("Filho Pato");
        dto.setMaeId(99L);

        when(patoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> patoService.criar(dto));
        verify(patoRepository, never()).save(any(Pato.class));
    }

    @Test
    void listarPatosVendidos_DeveChamarRepository() {
        // Arrange
        List<PatoVendidoDto> patosVendidos = List.of(new PatoVendidoDto());
        when(patoRepository.findPatosVendidosComDetalhes()).thenReturn(patosVendidos);

        // Act
        List<PatoVendidoDto> resultado = patoService.listarPatosVendidos();

        // Assert
        assertNotNull(resultado);
        verify(patoRepository, times(1)).findPatosVendidosComDetalhes();
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarPato() {
        // Arrange
        Pato pato = new Pato();
        pato.setId(1L);
        pato.setNome("Pato Teste");

        when(patoRepository.findById(1L)).thenReturn(Optional.of(pato));

        // Act
        PatoDto resultado = patoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pato Teste", resultado.getNome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(patoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> patoService.buscarPorId(1L));
    }

    @Test
    void deleteIfNotVendido_QuandoPatoNaoVendido_DeveDeletar() {
        // Arrange
        Pato pato = new Pato();
        pato.setId(1L);
        pato.setVendido(false);

        when(patoRepository.findById(1L)).thenReturn(Optional.of(pato));
        doNothing().when(patoRepository).delete(pato);

        // Act
        patoService.deleteIfNotVendido(1L);

        // Assert
        verify(patoRepository, times(1)).delete(pato);
    }

    @Test
    void deleteIfNotVendido_QuandoPatoVendido_DeveLancarExcecao() {
        // Arrange
        Pato pato = new Pato();
        pato.setId(1L);
        pato.setVendido(true);

        when(patoRepository.findById(1L)).thenReturn(Optional.of(pato));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> patoService.deleteIfNotVendido(1L));
        verify(patoRepository, never()).delete(any());
    }

}
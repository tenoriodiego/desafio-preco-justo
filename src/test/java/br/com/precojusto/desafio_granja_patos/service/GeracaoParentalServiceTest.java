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

import br.com.precojusto.desafio_granja_patos.dto.GeracaoParentalDto;
import br.com.precojusto.desafio_granja_patos.entity.GeracaoParental;
import br.com.precojusto.desafio_granja_patos.repository.GeracaoParentalRepository;

@ExtendWith(MockitoExtension.class)
class GeracaoParentalServiceTest {

    @Mock
    private GeracaoParentalRepository repository;

    @InjectMocks
    private GeracaoParentalService service;

    @Test
    void criar_DeveRetornarGeracaoParentalDto() {
        // Arrange
        GeracaoParentalDto dto = new GeracaoParentalDto();
        dto.setNome("Geração A");
        dto.setDescricao("Descrição teste");

        GeracaoParental entitySalva = new GeracaoParental();
        entitySalva.setId(1L);
        entitySalva.setNome("Geração A");
        entitySalva.setDescricao("Descrição teste");

        when(repository.save(any(GeracaoParental.class))).thenReturn(entitySalva);

        // Act
        GeracaoParentalDto resultado = service.criar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Geração A", resultado.getNome());
        assertEquals("Descrição teste", resultado.getDescricao());
        verify(repository, times(1)).save(any(GeracaoParental.class));
    }

    @Test
    void listar_DeveRetornarListaDeGeracoes() {
        // Arrange
        GeracaoParental entity1 = new GeracaoParental();
        entity1.setId(1L);
        entity1.setNome("Geração A");
        entity1.setDescricao("Descrição A");

        GeracaoParental entity2 = new GeracaoParental();
        entity2.setId(2L);
        entity2.setNome("Geração B");
        entity2.setDescricao("Descrição B");

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<GeracaoParentalDto> resultado = service.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Geração A", resultado.get(0).getNome());
        assertEquals("Geração B", resultado.get(1).getNome());
        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarGeracao() {
        // Arrange
        GeracaoParental entity = new GeracaoParental();
        entity.setId(1L);
        entity.setNome("Geração Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        GeracaoParentalDto resultado = service.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Geração Teste", resultado.getNome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.buscarPorId(1L));
    }

    @Test
    void deletar_QuandoExiste_DeveDeletarGeracao() {
        // Arrange
        GeracaoParental entity = new GeracaoParental();
        entity.setId(1L);
        entity.setNome("Geração Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(repository).delete(entity);

        // Act
        service.deletar(1L);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(entity);
    }

    @Test
    void deletar_QuandoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.deletar(1L));
        verify(repository, never()).delete(any());
    }
}
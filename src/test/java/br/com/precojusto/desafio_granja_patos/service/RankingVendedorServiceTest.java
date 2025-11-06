package br.com.precojusto.desafio_granja_patos.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.precojusto.desafio_granja_patos.dto.RankingVendedorDto;
import br.com.precojusto.desafio_granja_patos.entity.Vendedor;
import br.com.precojusto.desafio_granja_patos.repository.VendaRepository;
import br.com.precojusto.desafio_granja_patos.repository.VendedorRepository;

@ExtendWith(MockitoExtension.class)
class RankingVendedorServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @InjectMocks
    private RankingVendedorService service;

    @Test
    void rankingPorPeriodo_OrdenadoPorQuantidade_DeveRetornarRankingCorreto() {
        // Arrange
        Object[] row1 = new Object[] { 1L, 10L, BigDecimal.valueOf(1000.0) };
        Object[] row2 = new Object[] { 2L, 8L, BigDecimal.valueOf(800.0) };
        List<Object[]> rows = List.of(row1, row2);

        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId(1L);
        vendedor1.setNome("Vendedor A");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId(2L);
        vendedor2.setNome("Vendedor B");

        when(vendaRepository.aggregateByVendedor(any(), any())).thenReturn(rows);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor1));
        when(vendedorRepository.findById(2L)).thenReturn(Optional.of(vendedor2));

        // Act
        List<RankingVendedorDto> resultado = service.rankingPorPeriodo(
                OffsetDateTime.now(), OffsetDateTime.now(), "qtd");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Vendedor A", resultado.get(0).getVendedorNome());
        assertEquals(10L, resultado.get(0).getTotalVendas());
        assertEquals("Vendedor B", resultado.get(1).getVendedorNome());
        assertEquals(8L, resultado.get(1).getTotalVendas());
    }

    @Test
    void rankingPorPeriodo_OrdenadoPorValor_DeveRetornarRankingCorreto() {
        // Arrange
        Object[] row1 = new Object[] { 1L, 5L, BigDecimal.valueOf(500.0) };
        Object[] row2 = new Object[] { 2L, 3L, BigDecimal.valueOf(800.0) };
        List<Object[]> rows = List.of(row1, row2);

        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId(1L);
        vendedor1.setNome("Vendedor A");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId(2L);
        vendedor2.setNome("Vendedor B");

        when(vendaRepository.aggregateByVendedor(any(), any())).thenReturn(rows);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor1));
        when(vendedorRepository.findById(2L)).thenReturn(Optional.of(vendedor2));

        // Act
        List<RankingVendedorDto> resultado = service.rankingPorPeriodo(
                OffsetDateTime.now(), OffsetDateTime.now(), "valor");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Vendedor B", resultado.get(0).getVendedorNome()); // Maior valor primeiro
        assertEquals(BigDecimal.valueOf(800.0), resultado.get(0).getTotalValor());
    }

}
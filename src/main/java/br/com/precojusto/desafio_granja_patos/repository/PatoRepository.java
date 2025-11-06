package br.com.precojusto.desafio_granja_patos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.dto.PatoVendidoDto;
import br.com.precojusto.desafio_granja_patos.entity.Pato;

@Repository
public interface PatoRepository extends JpaRepository<Pato, Long> {
    List<Pato> findByMaeIsNull();

    List<Pato> findByMaeId(Long maeId);

    @Query("""
            SELECT new br.com.precojusto.desafio_granja_patos.dto.PatoVendidoDto(
                p.id,
                p.nome,
                v.dataVenda,
                vp.precoUnitario,
                c.nome,
                ven.nome
            )
            FROM Pato p
            JOIN VendaPato vp ON p.id = vp.pato.id
            JOIN vp.venda v
            JOIN v.cliente c
            JOIN v.vendedor ven
            WHERE p.vendido = true
            ORDER BY v.dataVenda DESC
            """)
    List<PatoVendidoDto> findPatosVendidosComDetalhes();

    // No PatoRepository
    @Query("SELECT p FROM Pato p LEFT JOIN FETCH p.mae")
    List<Pato> findAllWithMae();
}
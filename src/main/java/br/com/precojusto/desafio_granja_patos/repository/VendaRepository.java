package br.com.precojusto.desafio_granja_patos.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.entity.Venda;
import br.com.precojusto.desafio_granja_patos.entity.VendaPato;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("""
                SELECT DISTINCT v FROM Venda v
                LEFT JOIN FETCH v.itens vp
                LEFT JOIN FETCH vp.pato p
                LEFT JOIN FETCH v.cliente c
                LEFT JOIN FETCH v.vendedor ven
                WHERE v.dataVenda BETWEEN :inicio AND :fim
            """)
    List<Venda> findAllWithItensByDataVendaBetween(@Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim);

    List<Venda> findAllByDataVendaBetween(OffsetDateTime start, OffsetDateTime end);

    @Query("""
                SELECT v.vendedor.id, COUNT(v.id) AS totalVendas, SUM(v.valorTotal) AS totalValor
                FROM Venda v
                WHERE v.dataVenda BETWEEN :inicio AND :fim
                GROUP BY v.vendedor.id
            """)
    List<Object[]> aggregateByVendedor(@Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim);

    // NOVO: Busca vendas apenas com cliente e vendedor
    @Query("SELECT v FROM Venda v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.vendedor WHERE v.dataVenda BETWEEN :inicio AND :fim")
    List<Venda> findVendasComClienteVendedor(@Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim);

    // NOVO: Busca todos os itens com pato para uma lista de vendas
    @Query("SELECT vp FROM VendaPato vp JOIN FETCH vp.pato WHERE vp.venda.id IN :vendaIds")
    List<VendaPato> findItensComPatoPorVendaIds(@Param("vendaIds") List<Long> vendaIds);

    // NOVO MÉTODO: Busca todos os itens de venda com seus relacionamentos em uma
    // única query
    @Query("""
            SELECT vp FROM VendaPato vp
            JOIN FETCH vp.venda v
            JOIN FETCH v.cliente
            JOIN FETCH v.vendedor
            JOIN FETCH vp.pato
            WHERE v.dataVenda BETWEEN :inicio AND :fim
            """)
    List<VendaPato> findItensComPatoPorPeriodo(@Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim);

}

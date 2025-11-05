package br.com.precojusto.desafio_granja_patos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.entity.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    boolean existsByVendedorId(Long vendedorId);
}
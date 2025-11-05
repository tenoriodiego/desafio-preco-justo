package br.com.precojusto.desafio_granja_patos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.entity.VendaPato;
import br.com.precojusto.desafio_granja_patos.entity.VendaPato.VendaPatoId;

@Repository
public interface VendaPatoRepository extends JpaRepository<VendaPato, VendaPatoId> {
}
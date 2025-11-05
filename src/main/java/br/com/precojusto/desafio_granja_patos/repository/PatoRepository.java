package br.com.precojusto.desafio_granja_patos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.entity.Pato;

@Repository
public interface PatoRepository extends JpaRepository<Pato, Long> {
}
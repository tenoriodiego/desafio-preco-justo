package br.com.precojusto.desafio_granja_patos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.precojusto.desafio_granja_patos.entity.GeracaoParental;

public interface GeracaoParentalRepository extends JpaRepository<GeracaoParental, Long> {
}
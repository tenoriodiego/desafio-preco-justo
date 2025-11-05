package br.com.precojusto.desafio_granja_patos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.precojusto.desafio_granja_patos.entity.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByMatricula(String matricula);
}
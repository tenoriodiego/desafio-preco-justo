package br.com.precojusto.desafio_granja_patos.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pato")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mae_id")
    private Pato mae;

    @Column(name = "filho_count")
    private Integer filhoCount = 0;

    @Column(name = "preco_categ", nullable = false)
    private String precoCateg;

    @Column(name = "vendido")
    private boolean vendido = false;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

}

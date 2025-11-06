package br.com.precojusto.desafio_granja_patos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venda_pato")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaPato {

    @EmbeddedId
    private VendaPatoId id = new VendaPatoId();

    @MapsId("vendaId") // 🔹 Diz que este ManyToOne usa o campo vendaId da chave composta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @MapsId("patoId") // 🔹 Mesmo para o patoId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pato_id", nullable = false)
    private Pato pato;

    @Column(name = "preco_unitario", precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    // Classe interna para a chave composta
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendaPatoId implements Serializable {
        @Column(name = "venda_id")
        private Long vendaId;

        @Column(name = "pato_id")
        private Long patoId;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof VendaPatoId))
                return false;
            VendaPatoId that = (VendaPatoId) o;
            return Objects.equals(vendaId, that.vendaId) &&
                    Objects.equals(patoId, that.patoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vendaId, patoId);
        }
    }
}

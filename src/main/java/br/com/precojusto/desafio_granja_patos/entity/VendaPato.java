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
    private VendaPatoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vendaId")
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patoId")
    @JoinColumn(name = "pato_id")
    private Pato pato;

    @Column(name = "preco_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Embeddable
    public static class VendaPatoId implements Serializable {
        private Long vendaId;
        private Long patoId;

        public VendaPatoId() {
        }

        public Long getVendaId() {
            return vendaId;
        }

        public void setVendaId(Long vendaId) {
            this.vendaId = vendaId;
        }

        public Long getPatoId() {
            return patoId;
        }

        public void setPatoId(Long patoId) {
            this.patoId = patoId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof VendaPatoId))
                return false;
            VendaPatoId that = (VendaPatoId) o;
            return Objects.equals(getVendaId(), that.getVendaId()) &&
                    Objects.equals(getPatoId(), that.getPatoId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getVendaId(), getPatoId());
        }
    }

}

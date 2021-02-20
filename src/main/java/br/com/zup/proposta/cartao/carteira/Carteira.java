package br.com.zup.proposta.cartao.carteira;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCarteira tipoCarteira;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cartao cartao;

    @Deprecated
    Carteira(){}

    public Carteira(String email, TipoCarteira tipoCarteira, Cartao cartao) {
        this.email = email;
        this.tipoCarteira = tipoCarteira;
        this.cartao = cartao;
    }

    public TipoCarteira getTipoCarteira() {
        return tipoCarteira;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", tipoCarteira=" + tipoCarteira +
                ", cartao=" + cartao +
                '}';
    }
}

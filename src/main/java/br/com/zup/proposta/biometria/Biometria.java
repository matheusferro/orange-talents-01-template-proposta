package br.com.zup.proposta.biometria;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Lob
    @Column(nullable = false, unique = true)
    private String biometria;

    @ManyToOne
    private Cartao cartao;

    @Deprecated
    Biometria(){}

    public Biometria(@NotBlank String biometria, Cartao cartao) {
        this.biometria = biometria;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public String getBiometria() {
        return biometria;
    }

    @Override
    public String toString() {
        return "Biometria{" +
                "id=" + id +
                ", biometria='" + biometria + '\'' +
                ", cartao=" + cartao +
                '}';
    }
}

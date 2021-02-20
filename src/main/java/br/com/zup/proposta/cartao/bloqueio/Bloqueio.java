package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Cartao cartao;

    @NotBlank
    @Column(nullable = false)
    private String ipCliente;

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @CreationTimestamp
    private LocalDateTime dataBloqueio;

    @Deprecated
    Bloqueio(){}

    public Bloqueio(Cartao cartao, @NotBlank String ipCliente, @NotBlank String userAgent) {
        this.cartao = cartao;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }
}

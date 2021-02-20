package br.com.zup.proposta.cartao.viagem;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Viagem {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @NotNull
    @Column(nullable = false)
    private LocalDate terminoViagem;

    @NotBlank
    @Column(nullable = false)
    private String ipCliente;

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cartao cartao;

    @CreationTimestamp
    private LocalDateTime dataAviso;

    @Deprecated
    Viagem(){}

    public Viagem(@NotBlank String destino,
                  LocalDate terminoViagem,
                  @NotBlank String ipCliente,
                  @NotBlank String userAgent,
                  Cartao cartao) {
        this.destino = destino;
        this.terminoViagem = terminoViagem;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }
}

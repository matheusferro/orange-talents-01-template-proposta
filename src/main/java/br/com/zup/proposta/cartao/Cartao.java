package br.com.zup.proposta.cartao;

import br.com.zup.proposta.biometria.Biometria;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import br.com.zup.proposta.cartao.carteira.Carteira;
import br.com.zup.proposta.cartao.viagem.Viagem;
import br.com.zup.proposta.proposta.Proposta;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.util.Assert.notNull;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String numeroCartao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartaoStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Proposta proposta;

    @OneToMany(mappedBy = "cartao")
    private List<Biometria> biometria;

    @OneToOne(mappedBy = "cartao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Bloqueio bloqueio;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL)
    private List<Viagem> viagem;

    @OneToMany(mappedBy = "cartao",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Carteira> carteira;

    @Deprecated
    Cartao(){}

    public Cartao(@NotBlank String numeroCartao, Proposta proposta) {
        this.numeroCartao = numeroCartao;
        this.proposta = proposta;
        this.status = CartaoStatus.DESBLOQUEADO;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public CartaoStatus getStatus() {
        return status;
    }

    public List<Carteira> getCarteira() {
        return carteira;
    }

    public void bloquearCartao(@NotNull String clientHost, @NotNull String userAgent, CartaoRepository cartaoRepository) {
        notNull(clientHost, "O clientHost não pode ser null.");
        notNull(userAgent, "User-Agent não pode ser null.");
        notNull(cartaoRepository, "Repository não pode ser null.");

        this.status = CartaoStatus.BLOQUEADO;
        this.bloqueio = new Bloqueio(this, clientHost, userAgent);
        cartaoRepository.save(this);
    }

    public void adicionarViagem(Viagem viagem, CartaoRepository cartaoRepository) {
        notNull(viagem, "Não foi possível adicionar viagem.");
        notNull(cartaoRepository, "Repository não pode ser null.");

        this.viagem.add(viagem);
        cartaoRepository.save(this);
    }
}

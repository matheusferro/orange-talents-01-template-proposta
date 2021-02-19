package br.com.zup.proposta.cartao;

import br.com.zup.proposta.biometria.Biometria;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import br.com.zup.proposta.proposta.Proposta;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
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
    private String numeroCartao;

    @Enumerated(EnumType.STRING)
    private CartaoStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Proposta proposta;

    @OneToMany(mappedBy = "cartao")
    private List<Biometria> biometria;

    @OneToOne(mappedBy = "cartao", cascade = CascadeType.ALL)
    private Bloqueio bloqueio;

    @Deprecated
    Cartao(){}

    public Cartao(@NotBlank String numeroCartao, Proposta proposta) {
        this.numeroCartao = numeroCartao;
        this.proposta = proposta;
        this.status = CartaoStatus.DESBLOQUEADO;
    }

    public Bloqueio getBloqueio() {
        return bloqueio;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public CartaoStatus getStatus() {
        return status;
    }

    /**
     * Metodo espera ser invocado dentro de uma transação.
     * Para atualizar os dados do objeto e ser sincronizado futuramente.
     *
     * @param clientHost
     * @param request
     */
    public void bloquearCartao(@NotNull String clientHost, @NotNull HttpServletRequest request) {
        notNull(clientHost, "O clientHost não pode ser null.");
        notNull(request, "request não pode ser null.");

        this.status = CartaoStatus.BLOQUEADO;
        this.bloqueio = new Bloqueio(this, clientHost, request.getHeader("User-Agent"));
    }
}

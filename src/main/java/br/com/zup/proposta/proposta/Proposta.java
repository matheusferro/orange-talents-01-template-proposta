package br.com.zup.proposta.proposta;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.schedule.CartaoGeradoResponse;
import br.com.zup.proposta.proposta.analise.SolicitacaoAnaliseResponse;
import br.com.zup.proposta.proposta.endereco.Endereco;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.util.Assert.notNull;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Utilizando criptografia do postgresql. Necessário ter a variável encrypt.key
     * settada no postgresql.conf.
     *
     * https://vladmihalcea.com/how-to-encrypt-and-decrypt-data-with-hibernate/
     */
    @ColumnTransformer(
            read = "pgp_sym_decrypt(documento, current_setting('encrypt.key'))",
            write = "pgp_sym_encrypt(?, current_setting('encrypt.key'))"
    )
    @Column(unique = true, columnDefinition = "bytea")
    private String documento;

    @Min(value = 0)
    @Column(nullable = false)
    private BigDecimal salario;

    @NotNull
    @Embedded
    private Endereco endereco;

    @CreationTimestamp
    private LocalDateTime dataCriada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NAO_ELEGIVEL;

    @OneToOne(mappedBy = "proposta",cascade = CascadeType.ALL)
    private Cartao cartao;

    @Deprecated
    Proposta(){}

    public Proposta(@NotBlank String nome,
                    @Email String email,
                    @Valid @NotBlank String documento,
                    @Min(value = 0) BigDecimal salario,
                    @Valid @NotNull Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    /**
     * Define status com base no resultado recebido da api de verificação
     * de elegibilidade.
     * @param analiseResponse
     * @param propostaRepository
     */
    public void definirStatus(SolicitacaoAnaliseResponse analiseResponse, PropostaRepository propostaRepository) {
        notNull(analiseResponse, "Não é posspível definir status null.");
        notNull(propostaRepository, "Não é posspível definir status com repository null.");

        this.status = Status.getStatus(analiseResponse);
        propostaRepository.save(this);
    }

    /**
     * Metodo utilizado no scheduler para cadastro de número do cartão.
     * @param cartaoGerado
     * @param propostaRepository
     */
    public void definirCartao(CartaoGeradoResponse cartaoGerado, PropostaRepository propostaRepository) {
        notNull(cartaoGerado, "Não é possível definir null para o valor do cartão.");
        notNull(propostaRepository, "Repository invalido.");

        this.cartao = new Cartao(cartaoGerado.getId(), this);
        this.status = Status.CONCLUIDO;
        propostaRepository.save(this);
    }
}

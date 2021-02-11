package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.cartao.AnaliseClient;
import br.com.zup.proposta.proposta.cartao.SolicitacaoAnaliseRequest;
import br.com.zup.proposta.proposta.cartao.SolicitacaoAnaliseResponse;
import br.com.zup.proposta.proposta.endereco.Endereco;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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

    @Column(nullable = false, unique = true)
    private String documento;

    @Min(value = 0)
    @Column(nullable = false)
    private BigDecimal salario;

    @NotNull
    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NAO_ELEGIVEL;

    private String numeroCartao;

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
     * @param analise
     * @param propostaRepository
     */
    public void definirStatus(AnaliseClient analise, PropostaRepository propostaRepository) {
        try {
            SolicitacaoAnaliseResponse analiseResponse = analise.solicitacaoAnalise(new SolicitacaoAnaliseRequest(this));
            this.status = Status.getStatus(analiseResponse);
            propostaRepository.save(this);
        }catch(FeignException exception){
            Logger logger = LoggerFactory.getLogger(Proposta.class);
            logger.info("FeignException - CADASTRO DE DOCUMENTO COM INICIO 3.");
        }
    }

    /**
     * Metodo utilizado no scheduler para cadastro de número do cartão.
     * @param numeroCartao
     * @param propostaRepository
     */
    public void definirCartao(String numeroCartao, PropostaRepository propostaRepository) {
        notNull(numeroCartao, "Não é possível definir null para o valor do cartão.");
        notNull(propostaRepository, "Repository invalido.");
        this.numeroCartao = numeroCartao;
        this.status = Status.CONCLUIDO;
        propostaRepository.save(this);
    }
}

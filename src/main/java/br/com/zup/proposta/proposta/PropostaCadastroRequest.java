package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.endereco.Endereco;
import br.com.zup.proposta.proposta.endereco.EnderecoRequest;
import br.com.zup.proposta.validation.CPForCNPJ;
import br.com.zup.proposta.validation.UniqueValue;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.util.Assert.notNull;

public class PropostaCadastroRequest {

    @NotBlank
    @JsonProperty
    private String nome;

    @NotBlank
    @UniqueValue(domainClass = Proposta.class, fieldName = "email")
    @Email
    @JsonProperty
    private String email;

    @CPForCNPJ
    @NotBlank
    @JsonProperty
    private String documento;

    @NotNull
    @Positive
    @JsonProperty
    private BigDecimal salario;

    @NotNull
    @JsonProperty
    private EnderecoRequest endereco;

    public PropostaCadastroRequest(@NotBlank String nome,
                                   @NotBlank @Email String email,
                                   @NotBlank String documento,
                                   @NotNull @Positive BigDecimal salario,
                                   @Valid @NotNull EnderecoRequest endereco) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Proposta toModel() {
        Endereco endereco = this.endereco.toModel();
        return new Proposta(nome, email, documento, salario, endereco);
    }

    public boolean isDocumentoCadastrado(PropostaRepository repository) {
        return repository.existsByDocumento(this.documento);
    }
}

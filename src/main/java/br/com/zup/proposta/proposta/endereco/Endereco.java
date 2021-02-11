package br.com.zup.proposta.proposta.endereco;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Endereco {

    @NotBlank
    @Column(nullable = false)
    private String cep;

    @NotBlank
    @Column(nullable = false)
    private String logradouro;

    @NotBlank
    @Column(nullable = false)
    private String numero;

    private String complemento;

    @Deprecated
    Endereco(){}

    public Endereco(@NotBlank String cep,
                    @NotBlank String logradouro,
                    @NotBlank String numero,
                    String complemento) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
    }
}

package br.com.zup.proposta.proposta.endereco;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class EnderecoRequest {
    @NotBlank
    @JsonProperty
    private String cep;

    @NotBlank
    @JsonProperty
    private String logradouro;

    @NotBlank
    @JsonProperty
    private String numero;

    @JsonProperty
    private String complemento;

    public EnderecoRequest(@NotBlank String cep,
                           @NotBlank String logradouro,
                           @NotBlank String numero,
                           String complemento) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
    }

    public Endereco toModel() {
        return new Endereco(cep, logradouro, numero, complemento);
    }
}

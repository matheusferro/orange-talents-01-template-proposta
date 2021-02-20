package br.com.zup.proposta.cartao.carteira;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CadastroCarteiraResponseClient {

    @JsonProperty
    private String resultado;

    @JsonProperty
    private String id;
}

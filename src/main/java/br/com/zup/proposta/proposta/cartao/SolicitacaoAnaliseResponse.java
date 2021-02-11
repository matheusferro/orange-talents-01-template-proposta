package br.com.zup.proposta.proposta.cartao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolicitacaoAnaliseResponse {
    @JsonProperty
    private String documento;
    @JsonProperty
    private String nome;
    @JsonProperty
    private Long idProposta;
    @JsonProperty
    private String resultadoSolicitacao;

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}

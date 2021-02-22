package br.com.zup.proposta.proposta.analise;

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

    public SolicitacaoAnaliseResponse(String documento,
                                      String nome,
                                      Long idProposta,
                                      String resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}

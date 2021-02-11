package br.com.zup.proposta.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolicitacaoAnaliseRequest {
    @JsonProperty
    private String documento;
    @JsonProperty
    private String nome;
    @JsonProperty
    private Long idProposta;

    public SolicitacaoAnaliseRequest(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId();
    }
}

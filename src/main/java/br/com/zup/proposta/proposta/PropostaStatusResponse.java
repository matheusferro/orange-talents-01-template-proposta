package br.com.zup.proposta.proposta;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropostaStatusResponse {

    @JsonProperty
    private Status status;

    public PropostaStatusResponse(Status status) {
        this.status = status;
    }
}

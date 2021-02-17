package br.com.zup.proposta.biometria;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BiometriaConsultaResponse {

    @JsonProperty
    private String biometria;

    public BiometriaConsultaResponse(Biometria biometria) {
        this.biometria = biometria.getBiometria();
    }
}

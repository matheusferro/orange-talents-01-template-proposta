package br.com.zup.proposta.biometria;

import javax.validation.constraints.NotBlank;

public class BiometriaCadastroRequest {

    @NotBlank
    private String biometria;

    public String getBiometria() {
        return biometria;
    }
}

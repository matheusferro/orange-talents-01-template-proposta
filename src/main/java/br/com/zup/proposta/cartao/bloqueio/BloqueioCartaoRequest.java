package br.com.zup.proposta.cartao.bloqueio;

public class BloqueioCartaoRequest {

    private String sistemaResponsavel = "sistema-X";

    public BloqueioCartaoRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}

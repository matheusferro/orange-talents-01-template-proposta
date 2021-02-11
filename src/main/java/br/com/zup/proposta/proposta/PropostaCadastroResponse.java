package br.com.zup.proposta.proposta;

public class PropostaCadastroResponse {
    private String nome;
    public PropostaCadastroResponse(Proposta proposta) {
        this.nome = proposta.getNome();
    }

    public String getNome() {
        return nome;
    }
}

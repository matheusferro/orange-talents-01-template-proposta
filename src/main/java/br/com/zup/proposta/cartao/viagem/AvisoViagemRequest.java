package br.com.zup.proposta.cartao.viagem;

import br.com.zup.proposta.cartao.Cartao;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDate validoAte;

    public AvisoViagemRequest(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }


    public Viagem toModel(AvisoViagemRequest request, String clientHost, String userAgent, Cartao cartao) {
        return new Viagem(this.destino, this.validoAte, clientHost, userAgent, cartao);
    }
}

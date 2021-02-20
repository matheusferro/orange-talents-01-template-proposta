package br.com.zup.proposta.cartao.carteira;

import br.com.zup.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notNull;

public class CarteiraRequest {

    @NotBlank
    @Email
    @JsonProperty
    private String email;

    @NotNull
    @JsonProperty
    private TipoCarteira carteira;

    public CarteiraRequest(String email, TipoCarteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(this.email, this.carteira, cartao);
    }

    public boolean carteiraJaCadastrada(Optional<Cartao> cartao){
        notNull(cartao,"Não é possível verificar se a carteira já esta cadastrada.");
        return cartao.filter(
                cartao1 -> cartao1.getCarteira().stream().anyMatch(
                        carteira -> carteira.getTipoCarteira().equals(this.carteira)
                )
        ).isPresent();
    }
}

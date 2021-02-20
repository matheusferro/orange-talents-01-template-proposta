package br.com.zup.proposta.cartao.carteira;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class AssociarCarteiraController {

    private CarteiraRepository carteiraRepository;

    private CartaoClient cartaoClient;

    private CartaoRepository cartaoRepository;

    public AssociarCarteiraController(CarteiraRepository carteiraRepository,
                                      CartaoClient cartaoClient,
                                      CartaoRepository cartaoRepository) {
        this.carteiraRepository = carteiraRepository;
        this.cartaoClient = cartaoClient;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/api/cartao/{idCartao}/carteira")
    public ResponseEntity<?> associar(@PathVariable @Valid Long idCartao,
                                      @RequestBody @Valid CarteiraRequest request,
                                      UriComponentsBuilder uriBuilder){

        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

        if(cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(request.carteiraJaCadastrada(cartao)){
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            cartaoClient.cadastroCarteira(cartao.get().getNumeroCartao(),request);
            Carteira carteira = request.toModel(cartao.get());
            carteiraRepository.save(carteira);
            URI location = uriBuilder.path("/api/cartao/{idCarteira}/carteira/{idCarteira}").build(cartao.get().getId(),carteira.getId());
            return ResponseEntity.created(location).build();
        }catch (FeignException.UnprocessableEntity exp){
            return ResponseEntity.badRequest().build();
        }
    }
}

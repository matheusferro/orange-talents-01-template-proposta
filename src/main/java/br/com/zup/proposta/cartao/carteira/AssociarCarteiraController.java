package br.com.zup.proposta.cartao.carteira;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class AssociarCarteiraController {

    Logger logger = LoggerFactory.getLogger(AssociarCarteiraController.class);

    private CarteiraRepository carteiraRepository;

    private CartaoClient cartaoClient;

    private CartaoRepository cartaoRepository;

    private Tracer tracer;

    public AssociarCarteiraController(CarteiraRepository carteiraRepository,
                                      CartaoClient cartaoClient,
                                      CartaoRepository cartaoRepository,
                                      Tracer tracer) {
        this.carteiraRepository = carteiraRepository;
        this.cartaoClient = cartaoClient;
        this.cartaoRepository = cartaoRepository;
        this.tracer = tracer;
    }

    @PostMapping("/api/cartao/{idCartao}/carteira")
    public ResponseEntity<?> associar(@PathVariable @Valid Long idCartao,
                                      @RequestBody @Valid CarteiraRequest request,
                                      UriComponentsBuilder uriBuilder){

        /**
         *  MELHORANDO O TROUBLESHOOTING.
         */
        Span activeSpan = tracer.activeSpan();
        //TAGS
        activeSpan.setTag("idCartao", idCartao);
        //BAGGAGE
        activeSpan.setBaggageItem("traceID", "0001");
        //LOG
        activeSpan.log("associandoCarteira");

        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

        if(cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(request.carteiraJaCadastrada(cartao)){
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            logger.info("[INICIO] ASSOCIACAO DE CARTEIRA COM SISTEMA LEGADO");
            cartaoClient.cadastroCarteira(cartao.get().getNumeroCartao(),request);
            logger.info("[FIM] ASSOCIACAO DE CARTEIRA COM SISTEMA LEGADO");
            Carteira carteira = request.toModel(cartao.get());
            carteiraRepository.save(carteira);
            URI location = uriBuilder.path("/api/cartao/{idCarteira}/carteira/{idCarteira}").build(cartao.get().getId(),carteira.getId());
            return ResponseEntity.created(location).build();
        }catch (FeignException.UnprocessableEntity exp){
            return ResponseEntity.badRequest().build();
        }
    }
}

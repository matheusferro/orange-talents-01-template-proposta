package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.cartao.CartaoStatus;
import br.com.zup.proposta.cartao.service.ClientHostResolver;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class BloqueioCartaoController {

    private CartaoRepository cartaoRepository;

    private CartaoClient cartaoClient;

    private ClientHostResolver hostResolver;

    private Tracer tracer;

    public BloqueioCartaoController(CartaoRepository cartaoRepository,
                                    CartaoClient cartaoClient,
                                    ClientHostResolver hostResolver,
                                    Tracer tracer) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
        this.hostResolver = hostResolver;
        this.tracer = tracer;
    }

    @PostMapping(path = "/api/cartao/{idCartao}/bloqueio")
    public ResponseEntity<?> bloquearCartao(@PathVariable @Valid Long idCartao, HttpServletRequest request){

        /**
         *  MELHORANDO O TROUBLESHOOTING.
         */
        Span activeSpan = tracer.activeSpan();
        //TAGS
        activeSpan.setTag("idCartao", idCartao);
        //BAGGAGE
        activeSpan.setBaggageItem("traceID", "0001");
        //LOG
        activeSpan.log("bloqueioCartao");

        String clientHost = hostResolver.resolve();
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

        //Retornar 404 = cartão não encontrado.
        if(cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //Cartão já bloqueado - retornar 422 para erro de negócio.
        if(cartao.get().getStatus().equals(CartaoStatus.BLOQUEADO)){
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            cartaoClient.bloqueioCartao(cartao.get().getNumeroCartao(),
                    new BloqueioCartaoRequest("apiPropostas"));
            cartao.get().bloquearCartao(clientHost, request.getHeader("User-Agent"), cartaoRepository);
        }catch (FeignException.UnprocessableEntity exception){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}

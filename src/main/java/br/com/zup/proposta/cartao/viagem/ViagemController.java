package br.com.zup.proposta.cartao.viagem;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.cartao.CartaoStatus;
import br.com.zup.proposta.cartao.service.ClientHostResolver;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ViagemController {

    private ClientHostResolver hostResolver;

    private CartaoRepository cartaoRepository;

    private CartaoClient cartaoClient;

    public ViagemController(ClientHostResolver hostResolver,
                            CartaoRepository cartaoRepository,
                            CartaoClient cartaoClient) {
        this.hostResolver = hostResolver;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
    }

    @PostMapping("/api/cartao/{idCartao}/viagem")
    public ResponseEntity<?> avisoViagem(@PathVariable("idCartao") Long idCartao,
                                         @RequestBody @Valid AvisoViagemRequest request,
                                         HttpServletRequest requestInfo){
        String clientHost = hostResolver.resolve();
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);
        if(cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(cartao.get().getStatus().equals(CartaoStatus.BLOQUEADO) || cartao.get().getBloqueio() != null){
            return ResponseEntity.badRequest().build();
        }

        try{
            cartaoClient.avisoViagem(cartao.get().getNumeroCartao(), request);
            Viagem viagem = request.toModel(request, clientHost, requestInfo.getHeader("User-Agent"), cartao.get());
            cartao.get().adicionarViagem(viagem, cartaoRepository);
        }catch (FeignException.UnprocessableEntity exp){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}

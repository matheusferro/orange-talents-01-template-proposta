package br.com.zup.proposta.cartao;

import br.com.zup.proposta.cartao.bloqueio.BloqueioCartaoRequest;
import br.com.zup.proposta.cartao.bloqueio.BloqueioCartaoResponse;
import br.com.zup.proposta.cartao.carteira.CadastroCarteiraResponseClient;
import br.com.zup.proposta.cartao.carteira.CarteiraRequest;
import br.com.zup.proposta.cartao.viagem.AvisoViagemRequest;
import br.com.zup.proposta.cartao.viagem.AvisoViagemResponseClient;
import br.com.zup.proposta.cartao.schedule.CartaoGeradoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CartaoClient", url="${service.cartao.url}")
public interface CartaoClient {

    @GetMapping(path = "/api/cartoes?idProposta={idProposta}")
    CartaoGeradoResponse cartaoGerado(@PathVariable(value="idProposta") Long idProposta);

    @PostMapping(path = "/api/cartoes/{idCartao}/bloqueios")
    BloqueioCartaoResponse bloqueioCartao(@PathVariable(value = "idCartao") String idCartao, @RequestBody BloqueioCartaoRequest bloqueioRequest);

    @PostMapping(path = "/api/cartoes/{idCartao}/avisos")
    AvisoViagemResponseClient avisoViagem(@PathVariable(value = "idCartao") String idCartao, @RequestBody AvisoViagemRequest request);

    @PostMapping(path = "/api/cartoes/{idCartao}/carteiras")
    CadastroCarteiraResponseClient cadastroCarteira(@PathVariable(value = "idCartao") String idCartao, @RequestBody CarteiraRequest request);
}

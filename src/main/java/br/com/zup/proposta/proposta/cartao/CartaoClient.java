package br.com.zup.proposta.proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CartaoClient", url="${service.cartao.url}")
public interface CartaoClient {

    @GetMapping(path = "/api/cartoes?idProposta={idProposta}")
    CartaoGeradoResponse cartaoGerado(@PathVariable(value="idProposta") Long idProposta);

}

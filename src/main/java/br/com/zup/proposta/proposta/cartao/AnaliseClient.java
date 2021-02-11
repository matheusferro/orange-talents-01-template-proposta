package br.com.zup.proposta.proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="AnaliseClient", url="${service.analiseSolicitacao.url}")
public interface AnaliseClient {

    @RequestMapping(method= RequestMethod.GET, path = "/api/solicitacao")
    SolicitacaoAnaliseResponse solicitacaoAnalise(@RequestBody SolicitacaoAnaliseRequest request);

}

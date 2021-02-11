package br.com.zup.proposta.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.proposta.PropostaRepository;
import br.com.zup.proposta.proposta.Status;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartaoScheduler {

    private PropostaRepository propostaRepository;

    private CartaoClient cartaoClient;

    public CartaoScheduler(PropostaRepository propostaRepository, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(fixedRate = 60000)
    public void verificarCartaoDeCredito(){
        //TODO: Verificar se é bom mesmo utilizar List<Proposta> ou utilizar uma outra classe.

        List<Proposta> propostas = propostaRepository.findByStatusAndNumeroCartaoIsNull(Status.ELEGIVEL);

        for (Proposta proposta: propostas) {
            CartaoGeradoResponse response = null;

            try {
                response = cartaoClient.cartaoGerado(proposta.getId());
            }catch (FeignException exception){
                Logger logger = LoggerFactory.getLogger(CartaoScheduler.class);
                logger.info("FeignException - CARTÃO NAO ENCONTRADO.");
            }

            if(response != null
                    && !response.getId().isEmpty()
                    && proposta.getId().equals(response.getIdProposta())){
                proposta.definirCartao(response.getId(), propostaRepository);
            }
        }

    }
}

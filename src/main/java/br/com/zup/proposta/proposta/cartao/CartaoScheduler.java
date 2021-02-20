package br.com.zup.proposta.proposta.cartao;

import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.proposta.PropostaRepository;
import br.com.zup.proposta.proposta.Status;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class CartaoScheduler {

    private PropostaRepository propostaRepository;

    private CartaoClient cartaoClient;

    private TransactionTemplate transactionManager;

    public CartaoScheduler(PropostaRepository propostaRepository,
                           CartaoClient cartaoClient,
                           TransactionTemplate transactionManager) {
        this.propostaRepository = propostaRepository;
        this.cartaoClient = cartaoClient;
        this.transactionManager = transactionManager;
    }

    @Scheduled(fixedRate = 15000)
    public void verificarCartaoDeCredito(){
        boolean existePropostas = true;
        while(existePropostas) {
            //noinspection ConstantConditions
            existePropostas = transactionManager.execute((transactionStatus -> {
                List<Proposta> propostas = propostaRepository.findTop3ByStatusOrderByDataCriadaAsc(Status.ELEGIVEL);
                if (propostas.isEmpty()) {
                    return false;
                }
                for (Proposta proposta : propostas) {
                    try {
                        CartaoGeradoResponse response = cartaoClient.cartaoGerado(proposta.getId());
                        proposta.definirCartao(response, propostaRepository);
                    } catch (FeignException exception) {
                        Logger logger = LoggerFactory.getLogger(CartaoScheduler.class);
                        logger.info("FeignException - CARTÃO NAO ENCONTRADO.");
                    }
                }
                return true;
            }));
        }

    }
}

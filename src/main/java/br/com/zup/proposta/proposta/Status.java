package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.analise.SolicitacaoAnaliseResponse;

public enum Status {
    ELEGIVEL,
    NAO_ELEGIVEL,
    CONCLUIDO;

    static Status getStatus(SolicitacaoAnaliseResponse response){
        return response.getResultadoSolicitacao().equalsIgnoreCase("SEM_RESTRICAO") ? ELEGIVEL : NAO_ELEGIVEL;
    }
}

package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.analise.AnaliseClient;
import br.com.zup.proposta.proposta.analise.SolicitacaoAnaliseRequest;
import br.com.zup.proposta.proposta.analise.SolicitacaoAnaliseResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PropostaCadastroController {

    private final PropostaRepository propostaRepository;

    private final AnaliseClient analise;

    private Tracer tracer;

    public PropostaCadastroController(PropostaRepository propostaRepository,
                                      AnaliseClient analise,
                                      Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.analise = analise;
        this.tracer = tracer;
    }

    @PostMapping("/api/proposta")
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaCadastroRequest request,
                                   UriComponentsBuilder uriBuilder){
        /**
         *  MELHORANDO O TROUBLESHOOTING.
         */
        Span activeSpan = tracer.activeSpan();
        //TAGS
        activeSpan.setTag("user.email", request.getEmail());
        //BAGGAGE
        activeSpan.setBaggageItem("traceID", "0001");
        //LOG
        activeSpan.log("criarProposta");

        if(request.isDocumentoCadastrado(propostaRepository)){
            Map<String,String> response = new HashMap<>();
            response.put("mensagem", "Proposta já feita.");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        Proposta proposta = request.toModel();
        propostaRepository.save(proposta);

        try {
            SolicitacaoAnaliseResponse analiseResponse = analise.solicitacaoAnalise(new SolicitacaoAnaliseRequest(proposta));
            proposta.definirStatus(analiseResponse, propostaRepository);
        }catch(FeignException exception){
            Logger logger = LoggerFactory.getLogger(PropostaCadastroController.class);
            logger.info("FeignException - CADASTRO DE DOCUMENTO COM INICIO 3.");
        }

        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new PropostaCadastroResponse(proposta));
    }

}

package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.cartao.AnaliseClient;
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

    public PropostaCadastroController(PropostaRepository propostaRepository, AnaliseClient analise) {
        this.propostaRepository = propostaRepository;
        this.analise = analise;
    }

    @PostMapping("/api/proposta")
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaCadastroRequest request,
                                   UriComponentsBuilder uriBuilder){

        if(request.isDocumentoCadastrado(propostaRepository)){
            Map<String,String> response = new HashMap<>();
            response.put("mensagem", "Proposta já feita.");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        Proposta proposta = request.toModel();
        propostaRepository.save(proposta);

        proposta.definirStatus(analise, propostaRepository);

        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new PropostaCadastroResponse(proposta));
    }

}

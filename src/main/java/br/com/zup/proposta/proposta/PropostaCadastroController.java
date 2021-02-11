package br.com.zup.proposta.proposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class PropostaCadastroController {

    private final PropostaRepository propostaRepository;

    public PropostaCadastroController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @Transactional
    @PostMapping("/proposta")
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaCadastroRequest request,
                                   UriComponentsBuilder uriBuilder){
        System.out.println("[inicio]");
        Proposta proposta = request.toModel();
        propostaRepository.save(proposta);

        URI uri = uriBuilder.path("/proposta/{id}").buildAndExpand(proposta.getId()).toUri();

        return ResponseEntity.created(uri).body(new PropostaCadastroResponse(proposta));
    }
}

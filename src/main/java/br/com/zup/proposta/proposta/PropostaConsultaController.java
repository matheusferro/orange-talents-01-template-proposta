package br.com.zup.proposta.proposta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PropostaConsultaController {

    private PropostaRepository propostaRepository;

    public PropostaConsultaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping(value = "/proposta/{idProposta}")
    public ResponseEntity<PropostaStatusResponse> consultaStatus(@PathVariable("idProposta") Long idProposta){
        Optional<PropostaStatusResponse> response = propostaRepository.findStatusById(idProposta);

        if(response.isPresent()){
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.badRequest().build();
    }

}

package br.com.zup.proposta.biometria;

import br.com.zup.proposta.cartao.Cartao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Base64;

@RestController
public class BiometriaController {

    private EntityManager entityManager;

    public BiometriaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PostMapping("/biometria/{idCartao}")
    public ResponseEntity<?> criarBiometria(@PathVariable("idCartao") Long idCartao,
                                            @RequestBody @Valid BiometriaCadastroRequest request,
                                            UriComponentsBuilder uriBuilder) throws UnsupportedEncodingException {
        //Verifica se é Base64.
        try {
            byte[] decodedString = Base64.getDecoder().decode(request.getBiometria());
        }catch (IllegalArgumentException exp){
            return ResponseEntity.badRequest().build();
        }

        //Existencia do cartão
        Cartao cartao = entityManager.find(Cartao.class,idCartao);
        if(cartao == null){
            return ResponseEntity.notFound().build();
        }

        Biometria biometria = new Biometria(request.getBiometria(), cartao);
        entityManager.persist(biometria);

        URI location = uriBuilder.path("/biometria/{idBiometria}").buildAndExpand(biometria.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Transactional
    @GetMapping("/biometria/{idBiometria}")
    public ResponseEntity<BiometriaConsultaResponse> consultaBiometria(@PathVariable("idBiometria") Long idBiometria,
                                            UriComponentsBuilder uriBuilder) throws UnsupportedEncodingException {
        Biometria biometria = entityManager.find(Biometria.class, idBiometria);
        return ResponseEntity.ok().body(new BiometriaConsultaResponse(biometria));
    }
}

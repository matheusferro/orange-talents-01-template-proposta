package br.com.zup.proposta.biometria;

import br.com.zup.proposta.cartao.Cartao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.codec.binary.Base64;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
public class BiometriaController {

    private EntityManager entityManager;

    public BiometriaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @PostMapping("/api/biometria/{idCartao}")
    public ResponseEntity<?> criarBiometria(@PathVariable("idCartao") Long idCartao,
                                            @RequestBody @Valid BiometriaCadastroRequest request,
                                            UriComponentsBuilder uriBuilder) throws UnsupportedEncodingException {
        //Verifica se é Base64.
        if(!Base64.isBase64(request.getBiometria())){
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

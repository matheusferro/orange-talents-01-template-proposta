package br.com.zup.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    List<Proposta> findByStatusAndCartaoIsNull(Status elegivel);

    @Query("SELECT new br.com.zup.proposta.proposta.PropostaStatusResponse(p.status) FROM Proposta p WHERE p.id = :idProposta")
    Optional<PropostaStatusResponse> findStatusById(Long idProposta);
}

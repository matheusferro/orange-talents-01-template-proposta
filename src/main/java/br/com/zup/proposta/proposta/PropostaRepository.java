package br.com.zup.proposta.proposta;

import org.hibernate.LockOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    /**
     * Lockando a query para não retornar resultados repetidos em cenario de sistemas distribuidos
     *
     * @param elegivel
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = (LockOptions.SKIP_LOCKED + ""))
    })
    List<Proposta> findTop3ByStatusOrderByDataCriadaAsc(Status elegivel);

    @Query("SELECT new br.com.zup.proposta.proposta.PropostaStatusResponse(p.status) FROM Proposta p WHERE p.id = :idProposta")
    Optional<PropostaStatusResponse> findStatusById(Long idProposta);
}

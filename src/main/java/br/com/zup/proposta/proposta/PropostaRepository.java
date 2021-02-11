package br.com.zup.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    List<Proposta> findByStatusAndNumeroCartaoIsNull(Status elegivel);
}

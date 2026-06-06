package br.edu.tcc.crmassistente.repository;

import br.edu.tcc.crmassistente.model.Processo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {

    @EntityGraph(attributePaths = "cliente")
    Optional<Processo> findByNumero(String numero);

    @EntityGraph(attributePaths = "cliente")
    @Query("select p from Processo p join p.cliente c where c.nomeNormalizado = :nomeClienteNormalizado order by p.numero")
    List<Processo> listarPorNomeClienteNormalizado(@Param("nomeClienteNormalizado") String nomeClienteNormalizado);
}

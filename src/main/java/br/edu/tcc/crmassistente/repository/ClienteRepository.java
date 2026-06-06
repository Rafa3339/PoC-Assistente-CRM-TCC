package br.edu.tcc.crmassistente.repository;

import br.edu.tcc.crmassistente.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c where c.nomeNormalizado = :nomeNormalizado")
    Optional<Cliente> buscarPorNomeNormalizado(@Param("nomeNormalizado") String nomeNormalizado);
}

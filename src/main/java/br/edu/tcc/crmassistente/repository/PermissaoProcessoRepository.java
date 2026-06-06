package br.edu.tcc.crmassistente.repository;

import br.edu.tcc.crmassistente.model.PermissaoProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissaoProcessoRepository extends JpaRepository<PermissaoProcesso, Long> {

    @Query("""
            select count(pp) > 0
            from PermissaoProcesso pp
            where pp.usuario.username = :username
              and pp.processo.numero = :numeroProcesso
            """)
    boolean usuarioTemPermissaoNoProcesso(
            @Param("username") String username,
            @Param("numeroProcesso") String numeroProcesso
    );
}

package br.edu.tcc.crmassistente.service;

import br.edu.tcc.crmassistente.model.Intencao;
import br.edu.tcc.crmassistente.model.LogConsulta;
import br.edu.tcc.crmassistente.repository.LogConsultaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final LogConsultaRepository logConsultaRepository;

    public AuditLogService(LogConsultaRepository logConsultaRepository) {
        this.logConsultaRepository = logConsultaRepository;
    }

    public void registrar(String usuario, String pergunta, Intencao intencao, boolean autorizado) {
        logConsultaRepository.save(new LogConsulta(usuario, pergunta, intencao, autorizado, LocalDateTime.now()));
    }
}

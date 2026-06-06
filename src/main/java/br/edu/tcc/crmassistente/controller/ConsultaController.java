package br.edu.tcc.crmassistente.controller;

import br.edu.tcc.crmassistente.dto.AudioConsultaRequest;
import br.edu.tcc.crmassistente.dto.ConsultaRequest;
import br.edu.tcc.crmassistente.dto.ConsultaResponse;
import br.edu.tcc.crmassistente.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/consulta")
    public ConsultaResponse consultar(@Valid @RequestBody ConsultaRequest request) {
        return consultaService.consultar(request.getUsuario(), request.getPerfil(), request.getMensagem());
    }

    @PostMapping("/consulta-audio")
    public ConsultaResponse consultarAudio(@Valid @RequestBody AudioConsultaRequest request) {
        return consultaService.consultar(request.getUsuario(), request.getPerfil(), request.getTranscricaoSimulada());
    }
}

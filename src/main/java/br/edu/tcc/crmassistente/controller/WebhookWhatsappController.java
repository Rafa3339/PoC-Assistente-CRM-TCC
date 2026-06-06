package br.edu.tcc.crmassistente.controller;

import br.edu.tcc.crmassistente.dto.ConsultaResponse;
import br.edu.tcc.crmassistente.dto.WhatsappWebhookRequest;
import br.edu.tcc.crmassistente.dto.WhatsappWebhookResponse;
import br.edu.tcc.crmassistente.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook")
public class WebhookWhatsappController {

    private final ConsultaService consultaService;
    private final String verifyToken;

    public WebhookWhatsappController(ConsultaService consultaService,
                                     @Value("${whatsapp.webhook.verify-token}") String verifyToken) {
        this.consultaService = consultaService;
        this.verifyToken = verifyToken;
    }

    @GetMapping("/whatsapp")
    public ResponseEntity<String> verificarWebhook(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge
    ) {
        boolean tokenValido = "subscribe".equals(mode)
                && verifyToken.equals(token)
                && challenge != null;

        if (tokenValido) {
            return ResponseEntity.ok(challenge);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token de verificacao invalido.");
    }

    @PostMapping("/whatsapp")
    public WhatsappWebhookResponse receberMensagem(@Valid @RequestBody WhatsappWebhookRequest request) {
        ConsultaResponse consulta = consultaService.consultar(request.getUsuario(), null, request.getBody());
        return new WhatsappWebhookResponse(
                request.getFrom(),
                request.getBody(),
                consulta.getResposta(),
                consulta
        );
    }
}

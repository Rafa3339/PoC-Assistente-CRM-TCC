package br.edu.tcc.crmassistente.dto;

import jakarta.validation.constraints.NotBlank;

public class WhatsappWebhookRequest {

    @NotBlank
    private String from;

    @NotBlank
    private String body;

    @NotBlank
    private String usuario;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

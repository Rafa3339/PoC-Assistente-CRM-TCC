package br.edu.tcc.crmassistente.dto;

public class WhatsappWebhookResponse {

    private final String to;
    private final String mensagemRecebida;
    private final String respostaSimuladaWhatsapp;
    private final ConsultaResponse consulta;

    public WhatsappWebhookResponse(String to, String mensagemRecebida, String respostaSimuladaWhatsapp,
                                   ConsultaResponse consulta) {
        this.to = to;
        this.mensagemRecebida = mensagemRecebida;
        this.respostaSimuladaWhatsapp = respostaSimuladaWhatsapp;
        this.consulta = consulta;
    }

    public String getTo() {
        return to;
    }

    public String getMensagemRecebida() {
        return mensagemRecebida;
    }

    public String getRespostaSimuladaWhatsapp() {
        return respostaSimuladaWhatsapp;
    }

    public ConsultaResponse getConsulta() {
        return consulta;
    }
}

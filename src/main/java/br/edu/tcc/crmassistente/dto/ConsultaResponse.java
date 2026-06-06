package br.edu.tcc.crmassistente.dto;

import br.edu.tcc.crmassistente.model.Intencao;
import java.util.Map;

public class ConsultaResponse {

    private String perguntaOriginal;
    private Intencao intencaoDetectada;
    private Map<String, Object> entidades;
    private String resposta;
    private boolean autorizado;

    public ConsultaResponse(String perguntaOriginal, Intencao intencaoDetectada, Map<String, Object> entidades,
                            String resposta, boolean autorizado) {
        this.perguntaOriginal = perguntaOriginal;
        this.intencaoDetectada = intencaoDetectada;
        this.entidades = entidades;
        this.resposta = resposta;
        this.autorizado = autorizado;
    }

    public String getPerguntaOriginal() {
        return perguntaOriginal;
    }

    public Intencao getIntencaoDetectada() {
        return intencaoDetectada;
    }

    public Map<String, Object> getEntidades() {
        return entidades;
    }

    public String getResposta() {
        return resposta;
    }

    public boolean isAutorizado() {
        return autorizado;
    }
}

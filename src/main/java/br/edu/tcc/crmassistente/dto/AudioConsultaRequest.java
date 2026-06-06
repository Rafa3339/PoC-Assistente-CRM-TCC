package br.edu.tcc.crmassistente.dto;

import jakarta.validation.constraints.NotBlank;

public class AudioConsultaRequest {

    @NotBlank
    private String usuario;

    private String perfil;

    @NotBlank
    private String transcricaoSimulada;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTranscricaoSimulada() {
        return transcricaoSimulada;
    }

    public void setTranscricaoSimulada(String transcricaoSimulada) {
        this.transcricaoSimulada = transcricaoSimulada;
    }
}

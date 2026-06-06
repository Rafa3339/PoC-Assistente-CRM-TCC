package br.edu.tcc.crmassistente.dto;

import jakarta.validation.constraints.NotBlank;

public class ConsultaRequest {

    @NotBlank
    private String usuario;

    private String perfil;

    @NotBlank
    private String mensagem;

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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

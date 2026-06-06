package br.edu.tcc.crmassistente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_consulta")
public class LogConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false, length = 1000)
    private String pergunta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Intencao intencao;

    @Column(nullable = false)
    private boolean autorizado;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    protected LogConsulta() {
    }

    public LogConsulta(String usuario, String pergunta, Intencao intencao, boolean autorizado, LocalDateTime dataHora) {
        this.usuario = usuario;
        this.pergunta = pergunta;
        this.intencao = intencao;
        this.autorizado = autorizado;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPergunta() {
        return pergunta;
    }

    public Intencao getIntencao() {
        return intencao;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}

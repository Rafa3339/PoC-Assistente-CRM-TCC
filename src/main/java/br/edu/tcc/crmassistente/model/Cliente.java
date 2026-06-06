package br.edu.tcc.crmassistente.model;

import br.edu.tcc.crmassistente.util.TextNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nome_normalizado", nullable = false)
    private String nomeNormalizado;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    protected Cliente() {
    }

    public Cliente(String nome, String telefone, String email) {
        this.nome = nome;
        this.nomeNormalizado = TextNormalizer.normalizarParaBusca(nome);
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.nomeNormalizado = TextNormalizer.normalizarParaBusca(nome);
    }

    public String getNomeNormalizado() {
        return nomeNormalizado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

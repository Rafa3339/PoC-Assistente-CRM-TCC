package br.edu.tcc.crmassistente.dto;

import br.edu.tcc.crmassistente.model.Intencao;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class IntentInterpretation {

    private final Intencao intencao;
    private final Map<String, Object> entidades;

    public IntentInterpretation(Intencao intencao, Map<String, Object> entidades) {
        this.intencao = intencao;
        this.entidades = Collections.unmodifiableMap(new LinkedHashMap<>(entidades));
    }

    public Intencao getIntencao() {
        return intencao;
    }

    public Map<String, Object> getEntidades() {
        return entidades;
    }

    public String getEntidadeTexto(String chave) {
        Object valor = entidades.get(chave);
        return valor == null ? null : valor.toString();
    }
}

package br.edu.tcc.crmassistente.util;

import java.text.Normalizer;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static String normalizarParaBusca(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}

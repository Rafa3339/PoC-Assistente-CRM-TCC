package br.edu.tcc.crmassistente.service;

import br.edu.tcc.crmassistente.dto.IntentInterpretation;
import br.edu.tcc.crmassistente.model.Intencao;
import br.edu.tcc.crmassistente.util.TextNormalizer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class IntentInterpreterService {

    private static final Pattern NUMERO_PROCESSO_PATTERN = Pattern.compile("\\bprocesso\\s+(\\d+)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern CLIENTE_PATTERN = Pattern.compile("\\bcliente\\s+([\\p{L} .'-]+?)(?:[?.!]|$)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PROCESSOS_DO_CLIENTE_ORIGINAL_PATTERN = Pattern.compile(
            "\\bprocessos\\s+(?:s[aã]o\\s+)?(?:do|da|de)\\s+([\\p{L} .'-]+?)(?:[?.!]|$)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PROCESSOS_DO_CLIENTE_NORMALIZADO_PATTERN = Pattern.compile(
            "\\bprocessos\\s+(?:sao\\s+)?(?:do|da|de)\\s+([\\p{L} .'-]+?)(?:[?.!]|$)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public IntentInterpretation interpretar(String mensagem) {
        String normalizada = TextNormalizer.normalizarParaBusca(mensagem);
        Map<String, Object> entidades = new LinkedHashMap<>();

        if (normalizada.contains("processo")
                && contemAlguma(normalizada, "responsavel", "advogado", "cuida")) {
            return interpretarProcesso(mensagem, Intencao.BUSCAR_RESPONSAVEL_PROCESSO, entidades);
        }

        if (normalizada.contains("processo")
                && contemAlguma(normalizada, "status", "como esta", "situacao", "andamento")) {
            return interpretarProcesso(mensagem, Intencao.BUSCAR_STATUS_PROCESSO, entidades);
        }

        if (normalizada.contains("processos")
                && contemAlguma(normalizada, "cliente", "do ", "da ", "de ")) {
            extrairNomeClienteParaListagem(mensagem).ifPresent(nome -> entidades.put("nomeCliente", nome));
            if (entidades.containsKey("nomeCliente")) {
                return new IntentInterpretation(Intencao.LISTAR_PROCESSOS_CLIENTE, entidades);
            }
        }

        if (normalizada.contains("cliente")
                && contemAlguma(normalizada, "telefone", "dados", "email", "e-mail")) {
            extrairNomeCliente(mensagem).ifPresent(nome -> entidades.put("nomeCliente", nome));
            if (entidades.containsKey("nomeCliente")) {
                return new IntentInterpretation(Intencao.BUSCAR_DADOS_CLIENTE, entidades);
            }
        }

        return new IntentInterpretation(Intencao.NAO_RECONHECIDA, entidades);
    }

    private IntentInterpretation interpretarProcesso(String mensagem, Intencao intencao, Map<String, Object> entidades) {
        extrairNumeroProcesso(mensagem).ifPresent(numero -> entidades.put("numeroProcesso", numero));
        if (entidades.containsKey("numeroProcesso")) {
            return new IntentInterpretation(intencao, entidades);
        }
        return new IntentInterpretation(Intencao.NAO_RECONHECIDA, entidades);
    }

    private Optional<String> extrairNumeroProcesso(String mensagem) {
        Matcher matcher = NUMERO_PROCESSO_PATTERN.matcher(mensagem);
        return matcher.find() ? Optional.of(matcher.group(1).trim()) : Optional.empty();
    }

    private Optional<String> extrairNomeCliente(String mensagem) {
        Matcher matcher = CLIENTE_PATTERN.matcher(mensagem);
        return matcher.find() ? Optional.of(limparNomeCliente(matcher.group(1))) : Optional.empty();
    }

    private Optional<String> extrairNomeClienteParaListagem(String mensagem) {
        Matcher matcherOriginal = PROCESSOS_DO_CLIENTE_ORIGINAL_PATTERN.matcher(mensagem);
        if (matcherOriginal.find()) {
            return Optional.of(limparNomeCliente(matcherOriginal.group(1)));
        }

        Matcher matcherNormalizado = PROCESSOS_DO_CLIENTE_NORMALIZADO_PATTERN.matcher(
                TextNormalizer.normalizarParaBusca(mensagem)
        );
        if (matcherNormalizado.find()) {
            return Optional.of(limparNomeCliente(matcherNormalizado.group(1)));
        }

        return extrairNomeCliente(mensagem);
    }

    private String limparNomeCliente(String nome) {
        return nome.replaceAll("(?i)^cliente\\s+", "")
                .replaceAll("[?.!]+$", "")
                .trim();
    }

    private boolean contemAlguma(String texto, String... opcoes) {
        for (String opcao : opcoes) {
            if (texto.contains(opcao)) {
                return true;
            }
        }
        return false;
    }

}

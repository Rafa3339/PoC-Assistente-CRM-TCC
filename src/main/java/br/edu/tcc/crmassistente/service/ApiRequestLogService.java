package br.edu.tcc.crmassistente.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiRequestLogService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logFile;

    public ApiRequestLogService(@Value("${api.request-log.file:logs/api-requests.txt}") String logFile) {
        this.logFile = Paths.get(logFile);
    }

    public synchronized void registrar(ApiRequestLogEntry entry) {
        try {
            Path parent = logFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(
                    logFile,
                    formatar(entry),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel gravar o log de requisicoes da API.", exception);
        }
    }

    private String formatar(ApiRequestLogEntry entry) {
        return String.join(System.lineSeparator(),
                "=== API REQUEST ===",
                "dataHora=" + DATE_TIME_FORMATTER.format(entry.dataHora()),
                "metodo=" + entry.metodo(),
                "rota=" + entry.rota(),
                "query=" + valorOuVazio(entry.query()),
                "status=" + entry.status(),
                "duracaoMs=" + entry.duracaoMs(),
                "ip=" + valorOuVazio(entry.ip()),
                "corpo=" + valorOuVazio(entry.corpo()),
                ""
        );
    }

    private String valorOuVazio(String valor) {
        return valor == null ? "" : valor;
    }

    public record ApiRequestLogEntry(
            LocalDateTime dataHora,
            String metodo,
            String rota,
            String query,
            int status,
            long duracaoMs,
            String ip,
            String corpo
    ) {
    }
}

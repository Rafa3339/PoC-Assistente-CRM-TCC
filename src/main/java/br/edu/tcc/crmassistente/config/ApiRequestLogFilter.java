package br.edu.tcc.crmassistente.config;

import br.edu.tcc.crmassistente.service.ApiRequestLogService;
import br.edu.tcc.crmassistente.service.ApiRequestLogService.ApiRequestLogEntry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class ApiRequestLogFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_LENGTH = 5000;

    private final ApiRequestLogService apiRequestLogService;

    public ApiRequestLogFilter(ApiRequestLogService apiRequestLogService) {
        this.apiRequestLogService = apiRequestLogService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
        long inicio = System.currentTimeMillis();

        try {
            filterChain.doFilter(cachedRequest, response);
        } finally {
            long duracaoMs = System.currentTimeMillis() - inicio;
            apiRequestLogService.registrar(new ApiRequestLogEntry(
                    LocalDateTime.now(),
                    cachedRequest.getMethod(),
                    cachedRequest.getRequestURI(),
                    cachedRequest.getQueryString(),
                    response.getStatus(),
                    duracaoMs,
                    cachedRequest.getRemoteAddr(),
                    extrairCorpo(cachedRequest)
            ));
        }
    }

    private String extrairCorpo(ContentCachingRequestWrapper request) {
        byte[] conteudo = request.getContentAsByteArray();
        if (conteudo.length == 0) {
            return "";
        }

        Charset charset = request.getCharacterEncoding() == null
                ? StandardCharsets.UTF_8
                : Charset.forName(request.getCharacterEncoding());
        String corpo = new String(conteudo, charset)
                .replace("\r", "\\r")
                .replace("\n", "\\n");

        if (corpo.length() <= MAX_BODY_LENGTH) {
            return corpo;
        }

        return corpo.substring(0, MAX_BODY_LENGTH) + "...[truncado]";
    }
}

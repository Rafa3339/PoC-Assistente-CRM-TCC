package br.edu.tcc.crmassistente.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "api.request-log.file=target/test-api-requests.txt")
class ApiRequestLogFilterTest {

    private static final Path LOG_FILE = Path.of("target/test-api-requests.txt");

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void limparLog() throws Exception {
        Files.deleteIfExists(LOG_FILE);
    }

    @Test
    void deveRegistrarRequisicaoDaApiEmArquivoTxt() throws Exception {
        String corpo = """
                {
                  "usuario": "advogado_1",
                  "perfil": "ADVOGADO",
                  "mensagem": "Qual o status do processo 12345?"
                }
                """;

        mockMvc.perform(post("/api/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isOk());

        String log = Files.readString(LOG_FILE, StandardCharsets.UTF_8);

        assertThat(log).contains(
                "=== API REQUEST ===",
                "metodo=POST",
                "rota=/api/consulta",
                "status=200",
                "corpo="
        );
        assertThat(log).contains("advogado_1", "Qual o status do processo 12345?");
    }
}

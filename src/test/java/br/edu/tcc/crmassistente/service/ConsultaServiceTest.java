package br.edu.tcc.crmassistente.service;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.tcc.crmassistente.dto.ConsultaResponse;
import br.edu.tcc.crmassistente.model.Intencao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsultaServiceTest {

    @Autowired
    private ConsultaService consultaService;

    @Test
    void deveBuscarStatusDoProcesso() {
        ConsultaResponse response = consultaService.consultar(
                "advogado_1",
                "ADVOGADO",
                "Qual o status do processo 12345?"
        );

        assertThat(response.isAutorizado()).isTrue();
        assertThat(response.getIntencaoDetectada()).isEqualTo(Intencao.BUSCAR_STATUS_PROCESSO);
        assertThat(response.getResposta()).contains("Em andamento", "João Silva", "Dra. Ana Pereira");
    }

    @Test
    void deveBloquearProcessoSemPermissao() {
        ConsultaResponse response = consultaService.consultar(
                "assistente_1",
                "ADVOGADO",
                "Qual o status do processo 54321?"
        );

        assertThat(response.isAutorizado()).isFalse();
        assertThat(response.getResposta()).isEqualTo("Você não possui permissão para consultar este processo.");
    }

    @Test
    void deveBuscarDadosDoClienteMesmoSemAcentosNaPergunta() {
        ConsultaResponse response = consultaService.consultar(
                "advogado_1",
                "ADVOGADO",
                "Qual o telefone do cliente Joao Silva?"
        );

        assertThat(response.isAutorizado()).isTrue();
        assertThat(response.getIntencaoDetectada()).isEqualTo(Intencao.BUSCAR_DADOS_CLIENTE);
        assertThat(response.getResposta()).contains("João Silva", "(33) 99999-1111");
    }

    @Test
    void deveListarProcessosDoCliente() {
        ConsultaResponse response = consultaService.consultar(
                "advogado_1",
                "ADVOGADO",
                "Quais processos sao do cliente Maria Souza?"
        );

        assertThat(response.isAutorizado()).isTrue();
        assertThat(response.getIntencaoDetectada()).isEqualTo(Intencao.LISTAR_PROCESSOS_CLIENTE);
        assertThat(response.getResposta()).contains("54321", "Encerrado");
    }

    @Test
    void deveRetornarIntencaoNaoReconhecida() {
        ConsultaResponse response = consultaService.consultar(
                "advogado_1",
                "ADVOGADO",
                "Me diga alguma coisa aleatória"
        );

        assertThat(response.isAutorizado()).isTrue();
        assertThat(response.getIntencaoDetectada()).isEqualTo(Intencao.NAO_RECONHECIDA);
        assertThat(response.getResposta()).contains("Não consegui entender");
    }
}

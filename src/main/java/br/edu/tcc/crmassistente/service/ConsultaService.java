package br.edu.tcc.crmassistente.service;

import br.edu.tcc.crmassistente.dto.ConsultaResponse;
import br.edu.tcc.crmassistente.dto.IntentInterpretation;
import br.edu.tcc.crmassistente.model.Cliente;
import br.edu.tcc.crmassistente.model.Intencao;
import br.edu.tcc.crmassistente.model.Processo;
import br.edu.tcc.crmassistente.repository.ClienteRepository;
import br.edu.tcc.crmassistente.repository.ProcessoRepository;
import br.edu.tcc.crmassistente.util.TextNormalizer;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    private final IntentInterpreterService intentInterpreterService;
    private final PermissionService permissionService;
    private final ResponseFormatterService responseFormatterService;
    private final AuditLogService auditLogService;
    private final ProcessoRepository processoRepository;
    private final ClienteRepository clienteRepository;

    public ConsultaService(IntentInterpreterService intentInterpreterService,
                           PermissionService permissionService,
                           ResponseFormatterService responseFormatterService,
                           AuditLogService auditLogService,
                           ProcessoRepository processoRepository,
                           ClienteRepository clienteRepository) {
        this.intentInterpreterService = intentInterpreterService;
        this.permissionService = permissionService;
        this.responseFormatterService = responseFormatterService;
        this.auditLogService = auditLogService;
        this.processoRepository = processoRepository;
        this.clienteRepository = clienteRepository;
    }

    public ConsultaResponse consultar(String usuario, String perfil, String mensagem) {
        IntentInterpretation interpretacao = intentInterpreterService.interpretar(mensagem);

        return switch (interpretacao.getIntencao()) {
            case BUSCAR_STATUS_PROCESSO -> buscarStatusProcesso(usuario, perfil, mensagem, interpretacao);
            case BUSCAR_RESPONSAVEL_PROCESSO -> buscarResponsavelProcesso(usuario, perfil, mensagem, interpretacao);
            case BUSCAR_DADOS_CLIENTE -> buscarDadosCliente(usuario, mensagem, interpretacao);
            case LISTAR_PROCESSOS_CLIENTE -> listarProcessosCliente(usuario, perfil, mensagem, interpretacao);
            case NAO_RECONHECIDA -> responder(usuario, mensagem, interpretacao, responseFormatterService.intencaoNaoReconhecida(), true);
        };
    }

    private ConsultaResponse buscarStatusProcesso(String usuario, String perfil, String mensagem,
                                                  IntentInterpretation interpretacao) {
        String numeroProcesso = interpretacao.getEntidadeTexto("numeroProcesso");
        if (!permissionService.podeAcessarProcesso(usuario, perfil, numeroProcesso)) {
            return responder(usuario, mensagem, interpretacao, responseFormatterService.semPermissaoProcesso(), false);
        }

        String resposta = processoRepository.findByNumero(numeroProcesso)
                .map(responseFormatterService::statusProcesso)
                .orElse("Processo " + numeroProcesso + " não encontrado.");

        return responder(usuario, mensagem, interpretacao, resposta, true);
    }

    private ConsultaResponse buscarResponsavelProcesso(String usuario, String perfil, String mensagem,
                                                       IntentInterpretation interpretacao) {
        String numeroProcesso = interpretacao.getEntidadeTexto("numeroProcesso");
        if (!permissionService.podeAcessarProcesso(usuario, perfil, numeroProcesso)) {
            return responder(usuario, mensagem, interpretacao, responseFormatterService.semPermissaoProcesso(), false);
        }

        String resposta = processoRepository.findByNumero(numeroProcesso)
                .map(responseFormatterService::responsavelProcesso)
                .orElse("Processo " + numeroProcesso + " não encontrado.");

        return responder(usuario, mensagem, interpretacao, resposta, true);
    }

    private ConsultaResponse buscarDadosCliente(String usuario, String mensagem, IntentInterpretation interpretacao) {
        String nomeCliente = interpretacao.getEntidadeTexto("nomeCliente");
        String resposta = clienteRepository.buscarPorNomeNormalizado(TextNormalizer.normalizarParaBusca(nomeCliente))
                .map(responseFormatterService::dadosCliente)
                .orElse("Cliente " + nomeCliente + " não encontrado.");

        return responder(usuario, mensagem, interpretacao, resposta, true);
    }

    private ConsultaResponse listarProcessosCliente(String usuario, String perfil, String mensagem,
                                                    IntentInterpretation interpretacao) {
        String nomeCliente = interpretacao.getEntidadeTexto("nomeCliente");
        List<Processo> processos = processoRepository.listarPorNomeClienteNormalizado(
                TextNormalizer.normalizarParaBusca(nomeCliente)
        );
        List<Processo> processosAutorizados = processos.stream()
                .filter(processo -> permissionService.podeAcessarProcesso(usuario, perfil, processo.getNumero()))
                .toList();

        if (!processos.isEmpty() && processosAutorizados.isEmpty()) {
            return responder(usuario, mensagem, interpretacao, responseFormatterService.semPermissaoCliente(), false);
        }

        String resposta = responseFormatterService.processosCliente(nomeCliente, processosAutorizados);
        return responder(usuario, mensagem, interpretacao, resposta, true);
    }

    private ConsultaResponse responder(String usuario, String mensagem, IntentInterpretation interpretacao,
                                       String resposta, boolean autorizado) {
        auditLogService.registrar(usuario, mensagem, interpretacao.getIntencao(), autorizado);
        return new ConsultaResponse(mensagem, interpretacao.getIntencao(), interpretacao.getEntidades(),
                resposta, autorizado);
    }
}

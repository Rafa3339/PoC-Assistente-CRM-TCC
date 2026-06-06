package br.edu.tcc.crmassistente.service;

import br.edu.tcc.crmassistente.model.Cliente;
import br.edu.tcc.crmassistente.model.Processo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ResponseFormatterService {

    public String statusProcesso(Processo processo) {
        return "O processo " + processo.getNumero()
                + " está com status: " + processo.getStatus()
                + ". Cliente: " + processo.getCliente().getNome()
                + ". Responsável: " + processo.getResponsavel() + ".";
    }

    public String responsavelProcesso(Processo processo) {
        return "O responsável pelo processo " + processo.getNumero()
                + " é " + processo.getResponsavel()
                + ". Cliente: " + processo.getCliente().getNome() + ".";
    }

    public String dadosCliente(Cliente cliente) {
        return "Dados do cliente " + cliente.getNome()
                + ": telefone " + cliente.getTelefone()
                + ", e-mail " + cliente.getEmail() + ".";
    }

    public String processosCliente(String nomeCliente, List<Processo> processos) {
        if (processos.isEmpty()) {
            return "Não foram encontrados processos para o cliente " + nomeCliente + ".";
        }

        String lista = processos.stream()
                .map(processo -> processo.getNumero() + " (" + processo.getStatus() + ")")
                .collect(Collectors.joining("; "));

        return "Processos do cliente " + nomeCliente + ": " + lista + ".";
    }

    public String intencaoNaoReconhecida() {
        return "Não consegui entender a solicitação. Tente perguntar sobre status de processo, dados de cliente, "
                + "processos de um cliente ou responsável por um processo.";
    }

    public String semPermissaoProcesso() {
        return "Você não possui permissão para consultar este processo.";
    }

    public String semPermissaoCliente() {
        return "Você não possui permissão para consultar processos deste cliente.";
    }
}

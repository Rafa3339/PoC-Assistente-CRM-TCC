package br.edu.tcc.crmassistente.config;

import br.edu.tcc.crmassistente.model.Cliente;
import br.edu.tcc.crmassistente.model.PerfilUsuario;
import br.edu.tcc.crmassistente.model.PermissaoProcesso;
import br.edu.tcc.crmassistente.model.Processo;
import br.edu.tcc.crmassistente.model.Usuario;
import br.edu.tcc.crmassistente.repository.ClienteRepository;
import br.edu.tcc.crmassistente.repository.PermissaoProcessoRepository;
import br.edu.tcc.crmassistente.repository.ProcessoRepository;
import br.edu.tcc.crmassistente.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner carregarDadosIniciais(UsuarioRepository usuarioRepository,
                                            ClienteRepository clienteRepository,
                                            ProcessoRepository processoRepository,
                                            PermissaoProcessoRepository permissaoProcessoRepository) {
        return args -> {
            Usuario advogado = usuarioRepository.save(new Usuario("advogado_1", PerfilUsuario.ADVOGADO));
            Usuario assistente = usuarioRepository.save(new Usuario("assistente_1", PerfilUsuario.ASSISTENTE));

            Cliente joao = clienteRepository.save(new Cliente("João Silva", "(33) 99999-1111", "joao.silva@email.com"));
            Cliente maria = clienteRepository.save(new Cliente("Maria Souza", "(33) 99999-2222", "maria.souza@email.com"));

            Processo processoJoao = processoRepository.save(new Processo(
                    "12345",
                    "Em andamento",
                    joao,
                    "Dra. Ana Pereira",
                    "Ação de cobrança em fase de instrução."
            ));
            Processo processoMaria = processoRepository.save(new Processo(
                    "54321",
                    "Encerrado",
                    maria,
                    "Dr. Carlos Mendes",
                    "Processo trabalhista encerrado com acordo."
            ));

            permissaoProcessoRepository.save(new PermissaoProcesso(advogado, processoJoao));
            permissaoProcessoRepository.save(new PermissaoProcesso(advogado, processoMaria));
            permissaoProcessoRepository.save(new PermissaoProcesso(assistente, processoJoao));
        };
    }
}

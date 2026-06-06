package br.edu.tcc.crmassistente.service;

import br.edu.tcc.crmassistente.model.PerfilUsuario;
import br.edu.tcc.crmassistente.repository.PermissaoProcessoRepository;
import br.edu.tcc.crmassistente.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PermissionService {

    private final PermissaoProcessoRepository permissaoProcessoRepository;
    private final UsuarioRepository usuarioRepository;

    public PermissionService(PermissaoProcessoRepository permissaoProcessoRepository,
                             UsuarioRepository usuarioRepository) {
        this.permissaoProcessoRepository = permissaoProcessoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public boolean podeAcessarProcesso(String username, String perfilInformado, String numeroProcesso) {
        PerfilUsuario perfil = resolverPerfil(username, perfilInformado);

        if (perfil == PerfilUsuario.ADVOGADO) {
            return true;
        }

        if (perfil == PerfilUsuario.ASSISTENTE) {
            return permissaoProcessoRepository.usuarioTemPermissaoNoProcesso(username, numeroProcesso);
        }

        return false;
    }

    private PerfilUsuario resolverPerfil(String username, String perfilInformado) {
        PerfilUsuario perfilCadastrado = usuarioRepository.findByUsername(username)
                .map(usuario -> usuario.getPerfil())
                .orElse(null);

        if (perfilCadastrado != null) {
            return perfilCadastrado;
        }

        if (StringUtils.hasText(perfilInformado)) {
            try {
                return PerfilUsuario.valueOf(perfilInformado.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }

        return null;
    }
}

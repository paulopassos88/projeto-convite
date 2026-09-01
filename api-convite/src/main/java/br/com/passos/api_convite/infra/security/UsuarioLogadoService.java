package br.com.passos.api_convite.infra.security;

import br.com.passos.api_convite.domain.usuario.model.Usuario;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioLogadoService {

    public Optional<Usuario> getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se a autenticação existe e não é anônima
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // authentication.getPrincipal() retorna o objeto salvo no SecurityFilter
        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario usuario) {
            return Optional.of(usuario);
        }

        return Optional.empty();
    }

    public UUID getUsuarioLogadoId() {
        return getUsuarioLogado()
                .map(Usuario::getId)
                .orElseThrow(() -> new IllegalStateException("Nenhum usuário autenticado no contexto."));
    }
}

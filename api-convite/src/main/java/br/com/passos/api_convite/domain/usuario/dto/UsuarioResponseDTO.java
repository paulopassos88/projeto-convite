package br.com.passos.api_convite.domain.usuario.dto;

import br.com.passos.api_convite.domain.usuario.model.Perfil;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        Perfil perfil
) {}

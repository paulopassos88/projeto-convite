package br.com.passos.api_convite.domain.usuario.dto;

import br.com.passos.api_convite.domain.usuario.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados públicos do usuário cadastrado")
public record UsuarioResponseDTO(
        @Schema(description = "ID único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome completo do usuário", example = "João Organizador")
        String nome,

        @Schema(description = "E-mail do usuário", example = "organizador@evento.com")
        String email,

        @Schema(description = "Perfil de acesso do usuário", example = "ORGANIZADOR")
        Perfil perfil
) {}

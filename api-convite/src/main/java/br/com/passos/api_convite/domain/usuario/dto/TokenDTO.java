package br.com.passos.api_convite.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do token JWT gerado após autenticação")
public record TokenDTO(
        @Schema(description = "Token JWT assinado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tipo do token de autenticação", example = "Bearer")
        String tipo
) {}

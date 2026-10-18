package br.com.passos.api_convite.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para autenticação do usuário")
public record LoginDTO(
        @Schema(description = "E-mail do usuário cadastrado", example = "admin@teste.com")
        @NotBlank @Email String email,

        @Schema(description = "Senha do usuário", example = "123456")
        @NotBlank String senha
) {}

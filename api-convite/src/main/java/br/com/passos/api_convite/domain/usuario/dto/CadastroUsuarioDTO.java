package br.com.passos.api_convite.domain.usuario.dto;

import br.com.passos.api_convite.domain.usuario.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um novo usuário no sistema")
public record CadastroUsuarioDTO(
        @Schema(description = "Nome completo do usuário", example = "João Organizador")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,
        
        @Schema(description = "E-mail único de acesso", example = "organizador@evento.com")
        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "O e-mail deve ser válido")
        String email,
        
        @Schema(description = "Senha de acesso (mínimo de 6 caracteres)", example = "senhaSegura123")
        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,
        
        @Schema(description = "Perfil de permissão do usuário", example = "ORGANIZADOR")
        @NotNull(message = "O perfil é obrigatório")
        Perfil perfil
) {}

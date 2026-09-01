package br.com.passos.api_convite.domain.usuario.dto;

import br.com.passos.api_convite.domain.usuario.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,
        
        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "O e-mail deve ser válido")
        String email,
        
        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,
        
        @NotNull(message = "O perfil é obrigatório")
        Perfil perfil
) {}

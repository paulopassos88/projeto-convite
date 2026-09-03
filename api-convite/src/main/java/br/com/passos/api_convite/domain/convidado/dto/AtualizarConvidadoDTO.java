package br.com.passos.api_convite.domain.convidado.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizarConvidadoDTO(
        @NotBlank(message = "O nome do convidado é obrigatório")
        String nome,

        @NotBlank(message = "O e-mail do convidado é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,

        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres")
        String telefone,

        @NotNull(message = "A quantidade de acompanhantes permitidos é obrigatória")
        @Min(value = 0, message = "A quantidade de acompanhantes não pode ser negativa")
        Integer acompanhantesPermitidos,

        String observacoes
) {
}

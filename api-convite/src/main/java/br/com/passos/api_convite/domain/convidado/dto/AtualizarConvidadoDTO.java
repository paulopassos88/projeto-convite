package br.com.passos.api_convite.domain.convidado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização cadastral de um convidado")
public record AtualizarConvidadoDTO(
        @Schema(description = "Nome atualizado do convidado", example = "Carlos Eduardo Silva")
        @NotBlank(message = "O nome do convidado é obrigatório")
        String nome,

        @Schema(description = "E-mail atualizado do convidado", example = "carlos.eduardo@email.com")
        @NotBlank(message = "O e-mail do convidado é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,

        @Schema(description = "Telefone de contato atualizado", example = "+5511988887777")
        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres")
        String telefone,

        @Schema(description = "Nova quantidade de acompanhantes permitidos", example = "2")
        @NotNull(message = "A quantidade de acompanhantes permitidos é obrigatória")
        @Min(value = 0, message = "A quantidade de acompanhantes não pode ser negativa")
        Integer acompanhantesPermitidos,

        @Schema(description = "Observações atualizadas", example = "Restrição alimentar: sem glúten")
        String observacoes
) {
}

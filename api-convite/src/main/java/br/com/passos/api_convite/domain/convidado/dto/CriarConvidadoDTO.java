package br.com.passos.api_convite.domain.convidado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um convidado no evento")
public record CriarConvidadoDTO(
        @Schema(description = "Nome completo do convidado", example = "Carlos Silva")
        @NotBlank(message = "O nome do convidado é obrigatório")
        String nome,

        @Schema(description = "E-mail do convidado para recebimento do convite", example = "carlos@email.com")
        @NotBlank(message = "O e-mail do convidado é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,

        @Schema(description = "Telefone de contato opcional", example = "+5511999999999")
        @Size(max = 50, message = "O telefone deve ter no máximo 50 caracteres")
        String telefone,

        @Schema(description = "Quantidade de acompanhantes permitidos (deve respeitar o limite do evento)", example = "1")
        @NotNull(message = "A quantidade de acompanhantes permitidos é obrigatória")
        @Min(value = 0, message = "A quantidade de acompanhantes não pode ser negativa")
        Integer acompanhantesPermitidos,

        @Schema(description = "Observações opcionais sobre o convidado", example = "Mesa VIP")
        String observacoes
) {
}

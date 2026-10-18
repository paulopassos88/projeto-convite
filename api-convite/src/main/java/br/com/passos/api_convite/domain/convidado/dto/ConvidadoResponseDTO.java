package br.com.passos.api_convite.domain.convidado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de resposta de um convidado vinculado ao evento")
public record ConvidadoResponseDTO(
        @Schema(description = "ID único do convidado", example = "b4f1a2c3-d4e5-6789-abcd-ef0123456789")
        UUID id,
        @Schema(description = "ID do evento ao qual pertence", example = "7f3c9b12-8a4e-4d1c-9e2f-1a2b3c4d5e6f")
        UUID eventoId,
        @Schema(description = "Nome do convidado", example = "Carlos Silva")
        String nome,
        @Schema(description = "E-mail do convidado", example = "carlos@email.com")
        String email,
        @Schema(description = "Telefone do convidado", example = "+5511999999999")
        String telefone,
        @Schema(description = "Quantidade de acompanhantes permitidos", example = "1")
        Integer acompanhantesPermitidos,
        @Schema(description = "Observações registradas", example = "Mesa VIP")
        String observacoes,
        @Schema(description = "Data e hora de cadastro", example = "2026-10-16T11:30:00")
        LocalDateTime criadoEm
) {
}

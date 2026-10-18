package br.com.passos.api_convite.domain.convite.dto;

import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.model.StatusEnvio;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de resposta de um convite emitido")
public record ConviteResponseDTO(
        @Schema(description = "ID único do convite", example = "c9a8b7d6-e5f4-3210-9876-543210fedcba")
        UUID id,
        @Schema(description = "ID do convidado titular", example = "b4f1a2c3-d4e5-6789-abcd-ef0123456789")
        UUID convidadoId,
        @Schema(description = "Nome do convidado titular", example = "Carlos Silva")
        String nomeConvidado,
        @Schema(description = "E-mail do convidado titular", example = "carlos@email.com")
        String emailConvidado,
        @Schema(description = "ID do evento", example = "7f3c9b12-8a4e-4d1c-9e2f-1a2b3c4d5e6f")
        UUID eventoId,
        @Schema(description = "Nome do evento", example = "Casamento Real")
        String nomeEvento,
        @Schema(description = "Código único de acesso para apresentação na portaria", example = "7KM9XP2A")
        String codigo,
        @Schema(description = "URL opcional do QR Code", example = "null")
        String qrCodeUrl,
        @Schema(description = "Status de validação do convite", example = "PENDENTE")
        StatusConvite status,
        @Schema(description = "Status do envio por e-mail", example = "ENVIADO")
        StatusEnvio statusEnvio,
        @Schema(description = "Data e hora do último envio por e-mail", example = "2026-10-16T12:00:00")
        LocalDateTime enviadoEm,
        @Schema(description = "Data e hora de criação do convite", example = "2026-10-16T11:45:00")
        LocalDateTime criadoEm
) {}

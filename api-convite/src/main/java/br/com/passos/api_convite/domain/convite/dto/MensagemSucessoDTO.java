package br.com.passos.api_convite.domain.convite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Confirmação de enfileiramento assíncrono de envio de e-mail")
public record MensagemSucessoDTO(
        @Schema(description = "Mensagem descritiva do resultado", example = "Envio de e-mail do convite enfileirado com sucesso.")
        String mensagem,
        @Schema(description = "ID do convite enfileirado", example = "c9a8b7d6-e5f4-3210-9876-543210fedcba")
        UUID conviteId,
        @Schema(description = "Data e hora do enfileiramento", example = "2026-10-16T12:00:00")
        LocalDateTime enfileiradoEm
) {}

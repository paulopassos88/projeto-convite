package br.com.passos.api_convite.domain.convite.dto;

import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.model.StatusEnvio;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConviteResponseDTO(
        UUID id,
        UUID convidadoId,
        String nomeConvidado,
        String emailConvidado,
        UUID eventoId,
        String nomeEvento,
        String codigo,
        String qrCodeUrl,
        StatusConvite status,
        StatusEnvio statusEnvio,
        LocalDateTime enviadoEm,
        LocalDateTime criadoEm
) {}

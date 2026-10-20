package br.com.passos.api_convite.domain.convite.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConviteEmailPayloadDTO(
        UUID conviteId,
        String codigo,
        String convidadoNome,
        String convidadoEmail,
        String eventoNome,
        String eventoDescricao,
        String localNome,
        String endereco,
        LocalDateTime dataInicio,
        LocalDateTime dataTermino,
        String linkLocalizacao
) {}

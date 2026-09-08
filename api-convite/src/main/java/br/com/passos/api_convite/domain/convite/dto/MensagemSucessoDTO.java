package br.com.passos.api_convite.domain.convite.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemSucessoDTO(
        String mensagem,
        UUID conviteId,
        LocalDateTime enfileiradoEm
) {}

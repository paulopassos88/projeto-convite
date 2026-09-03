package br.com.passos.api_convite.domain.convidado.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConvidadoResponseDTO(
        UUID id,
        UUID eventoId,
        String nome,
        String email,
        String telefone,
        Integer acompanhantesPermitidos,
        String observacoes,
        LocalDateTime criadoEm
) {
}

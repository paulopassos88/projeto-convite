package br.com.passos.api_convite.domain.evento.dto;

import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventoResponseDTO(
        UUID id,
        String organizadorNome,
        String nome,
        String descricao,
        String localNome,
        String endereco,
        String linkMaps,
        LocalDateTime dataInicio,
        LocalDateTime dataTermino,
        Integer antecedenciaMinutos,
        Integer toleranciaAtrasoMinutos,
        Integer acompanhantesPadrao,
        StatusEvento status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}

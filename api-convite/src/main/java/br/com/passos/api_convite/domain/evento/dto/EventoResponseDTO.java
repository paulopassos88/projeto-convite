package br.com.passos.api_convite.domain.evento.dto;

import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados de resposta detalhados de um evento")
public record EventoResponseDTO(
        @Schema(description = "ID único do evento", example = "7f3c9b12-8a4e-4d1c-9e2f-1a2b3c4d5e6f")
        UUID id,
        @Schema(description = "Nome do organizador responsável pelo evento", example = "João Organizador")
        String organizadorNome,
        @Schema(description = "Nome do evento", example = "Casamento Real")
        String nome,
        @Schema(description = "Descrição do evento", example = "Cerimônia e festa de recepção")
        String descricao,
        @Schema(description = "Nome do local", example = "Villa Bisutti")
        String localNome,
        @Schema(description = "Endereço do local", example = "Rua das Flores, 123 - São Paulo, SP")
        String endereco,
        @Schema(description = "Link de localização", example = "https://maps.google.com/?q=Villa+Bisutti")
        String linkMaps,
        @Schema(description = "Data e hora de início", example = "2026-12-20T19:00:00")
        LocalDateTime dataInicio,
        @Schema(description = "Data e hora de término", example = "2026-12-21T02:00:00")
        LocalDateTime dataTermino,
        @Schema(description = "Antecedência máxima em minutos", example = "30")
        Integer antecedenciaMinutos,
        @Schema(description = "Tolerância máxima de atraso em minutos", example = "45")
        Integer toleranciaAtrasoMinutos,
        @Schema(description = "Limite máximo de acompanhantes por convidado", example = "2")
        Integer acompanhantesPadrao,
        @Schema(description = "Status atual do evento", example = "ATIVO")
        StatusEvento status,
        @Schema(description = "Data e hora de criação do registro", example = "2026-10-16T10:00:00")
        LocalDateTime criadoEm,
        @Schema(description = "Data e hora da última atualização", example = "2026-10-16T10:00:00")
        LocalDateTime atualizadoEm
) {
}

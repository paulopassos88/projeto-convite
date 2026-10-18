package br.com.passos.api_convite.domain.evento.dto;

import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados para atualização de um evento existente")
public record AtualizarEventoDTO(
        @Schema(description = "Nome atualizado do evento", example = "Casamento Real - Edição Especial")
        @NotBlank(message = "O nome do evento é obrigatório")
        String nome,

        @Schema(description = "Descrição atualizada do evento", example = "Cerimônia religiosa e festa")
        String descricao,

        @Schema(description = "Nome do local", example = "Villa Bisutti")
        @NotBlank(message = "O nome do local é obrigatório")
        String localNome,

        @Schema(description = "Endereço completo do local", example = "Rua das Flores, 123 - São Paulo, SP")
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @Schema(description = "Link opcional para o Google Maps ou Waze", example = "https://maps.google.com/?q=Villa+Bisutti")
        String linkMaps,

        @Schema(description = "Data e hora de início do evento", example = "2026-12-20T19:00:00")
        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode estar no passado")
        LocalDateTime dataInicio,

        @Schema(description = "Data e hora de término do evento", example = "2026-12-21T02:00:00")
        @NotNull(message = "A data de término é obrigatória")
        @Future(message = "A data de término deve estar no futuro")
        LocalDateTime dataTermino,

        @Schema(description = "Antecedência máxima em minutos permitida para entrada", example = "30")
        @NotNull(message = "A antecedência em minutos é obrigatória")
        @Min(value = 0, message = "A antecedência não pode ser negativa")
        Integer antecedenciaMinutos,

        @Schema(description = "Tolerância máxima de atraso em minutos após o início", example = "60")
        @NotNull(message = "A tolerância de atraso é obrigatória")
        @Min(value = 0, message = "A tolerância não pode ser negativa")
        Integer toleranciaAtrasoMinutos,

        @Schema(description = "Limite máximo de acompanhantes permitidos por convidado", example = "3")
        @NotNull(message = "O número padrão de acompanhantes é obrigatório")
        @Min(value = 0, message = "O número de acompanhantes não pode ser negativo")
        Integer acompanhantesPadrao,

        @Schema(description = "Status do evento", example = "ATIVO")
        @NotNull(message = "O status do evento é obrigatório")
        StatusEvento status
) {
}

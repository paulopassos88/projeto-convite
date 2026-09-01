package br.com.passos.api_convite.domain.evento.dto;

import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AtualizarEventoDTO(
        @NotBlank(message = "O nome do evento é obrigatório")
        String nome,

        String descricao,

        @NotBlank(message = "O nome do local é obrigatório")
        String localNome,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        String linkMaps,

        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode estar no passado")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de término é obrigatória")
        @Future(message = "A data de término deve estar no futuro")
        LocalDateTime dataTermino,

        @NotNull(message = "A antecedência em minutos é obrigatória")
        @Min(value = 0, message = "A antecedência não pode ser negativa")
        Integer antecedenciaMinutos,

        @NotNull(message = "A tolerância de atraso é obrigatória")
        @Min(value = 0, message = "A tolerância não pode ser negativa")
        Integer toleranciaAtrasoMinutos,

        @NotNull(message = "O número padrão de acompanhantes é obrigatório")
        @Min(value = 0, message = "O número de acompanhantes não pode ser negativo")
        Integer acompanhantesPadrao,

        @NotNull(message = "O status do evento é obrigatório")
        StatusEvento status
) {
}

package br.com.passos.api_convite.domain.convite.controller;

import br.com.passos.api_convite.domain.convite.dto.ConviteResponseDTO;
import br.com.passos.api_convite.domain.convite.dto.MensagemSucessoDTO;
import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.service.ConviteService;
import br.com.passos.api_convite.infra.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
@Tag(name = "5. Convites", description = "Geração de convites com token único, listagem, disparo assíncrono por e-mail (RabbitMQ) e revogação")
public class ConviteController {

    private final ConviteService conviteService;

    @PostMapping("/eventos/{eventoId}/convidados/{convidadoId}/convite")
    @Operation(
            summary = "Gerar ou reemitir convite para um convidado",
            description = "Gera um convite com token único e status PENDENTE para o convidado. Caso exista um convite REVOGADO, realiza a reemissão gerando novo token e reativando para PENDENTE (RN-009, RN-010, RN-011)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Convite gerado ou reemitido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Evento cancelado ou inválido para geração de convite",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para gerenciar convites deste evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou convidado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Convidado já possui um convite ativo (não revogado)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConviteResponseDTO> gerar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convidado", example = "770e8400-e29b-41d4-a716-446655440002")
            @PathVariable UUID convidadoId) {
        ConviteResponseDTO response = conviteService.gerar(eventoId, convidadoId);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/eventos/{eventoId}/convites")
    @Operation(
            summary = "Listar convites do evento",
            description = "Lista todos os convites de um evento de forma paginada, com filtro opcional por status (PENDENTE, CONFIRMADO, RECUSADO, REVOGADO)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de convites retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para visualizar convites deste evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<ConviteResponseDTO>> listar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "Filtro opcional por status do convite", example = "PENDENTE")
            @RequestParam(required = false) StatusConvite status,
            @ParameterObject @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable) {
        Page<ConviteResponseDTO> response = conviteService.listar(eventoId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eventos/{eventoId}/convites/{id}")
    @Operation(
            summary = "Buscar convite por ID",
            description = "Retorna os detalhes de um convite específico pertencente ao evento informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convite encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou convite não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConviteResponseDTO> buscarPorId(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convite", example = "880e8400-e29b-41d4-a716-446655440003")
            @PathVariable UUID id) {
        ConviteResponseDTO response = conviteService.buscarPorId(eventoId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eventos/{eventoId}/convidados/{convidadoId}/convite")
    @Operation(
            summary = "Buscar convite por ID do convidado",
            description = "Retorna o convite associado a um convidado específico dentro de um evento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convite encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento, convidado ou convite não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConviteResponseDTO> buscarPorConvidadoId(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convidado", example = "770e8400-e29b-41d4-a716-446655440002")
            @PathVariable UUID convidadoId) {
        ConviteResponseDTO response = conviteService.buscarPorConvidadoId(eventoId, convidadoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convites/{id}/enviar")
    @Operation(
            summary = "Solicitar envio de convite por e-mail",
            description = "Publica uma mensagem na fila RabbitMQ para disparo assíncrono do convite por e-mail ao convidado. Não permitido para convites REVOGADOS ou eventos CANCELADOS."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação de envio enfileirada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Convite revogado ou evento cancelado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para enviar este convite",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Convite não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<MensagemSucessoDTO> enviar(
            @Parameter(description = "ID único do convite", example = "880e8400-e29b-41d4-a716-446655440003")
            @PathVariable UUID id) {
        MensagemSucessoDTO response = conviteService.solicitarEnvio(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/convites/{id}/revogar")
    @Operation(
            summary = "Revogar convite",
            description = "Altera o status do convite para REVOGADO, invalidando o token atual para confirmações de presença ou envios (RN-011)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Convite revogado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Convite já se encontra revogado ou evento está cancelado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para revogar este convite",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Convite não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> revogar(
            @Parameter(description = "ID único do convite", example = "880e8400-e29b-41d4-a716-446655440003")
            @PathVariable UUID id) {
        conviteService.revogarConvite(id);
        return ResponseEntity.noContent().build();
    }
}


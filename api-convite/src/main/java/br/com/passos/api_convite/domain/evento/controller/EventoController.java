package br.com.passos.api_convite.domain.evento.controller;

import br.com.passos.api_convite.domain.evento.dto.AtualizarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.EventoResponseDTO;
import br.com.passos.api_convite.domain.evento.service.EventoService;
import br.com.passos.api_convite.infra.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/eventos")
@RequiredArgsConstructor
@Tag(name = "3. Eventos", description = "Gerenciamento do ciclo de vida dos eventos (criação, consulta, edição e cancelamento)")
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Operation(
            summary = "Criar novo evento",
            description = "Cadastra um novo evento vinculado ao usuário autenticado como organizador. A data de início deve ser futura e anterior à data de fim."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos ou datas inválidas (RN-005)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado (Token JWT ausente ou inválido)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Requer perfil ADMIN ou ORGANIZADOR)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EventoResponseDTO> criar(@RequestBody @Valid CriarEventoDTO dto) {
        EventoResponseDTO response = eventoService.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Operation(
            summary = "Listar eventos paginados",
            description = "Lista eventos ativos com paginação. Usuários ADMIN visualizam todos os eventos ativos; usuários ORGANIZADOR visualizam apenas os próprios eventos (RN-004)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de eventos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<EventoResponseDTO>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataInicio") Pageable pageable) {
        Page<EventoResponseDTO> response = eventoService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Operation(
            summary = "Buscar evento por ID",
            description = "Retorna os detalhes de um evento específico. O usuário deve ser ADMIN ou o organizador dono do evento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para visualizar este evento (não é o organizador dono nem ADMIN)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EventoResponseDTO> buscarPorId(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        EventoResponseDTO response = eventoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Operation(
            summary = "Atualizar evento",
            description = "Atualiza os dados de um evento ativo. Não é permitido atualizar eventos cancelados (RN-006) nem reduzir o limite de convidados abaixo do total já alocado (RN-008)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação, evento cancelado ou novo limite inferior ao total já alocado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para alterar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EventoResponseDTO> atualizar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarEventoDTO dto) {
        EventoResponseDTO response = eventoService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Operation(
            summary = "Cancelar evento (Soft Delete)",
            description = "Realiza o cancelamento lógico do evento alterando seu status para CANCELADO (RN-006)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Evento já se encontra cancelado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para cancelar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        eventoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}


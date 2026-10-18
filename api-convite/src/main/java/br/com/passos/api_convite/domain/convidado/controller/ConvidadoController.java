package br.com.passos.api_convite.domain.convidado.controller;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.dto.ConvidadoResponseDTO;
import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.service.ConvidadoService;
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
@RequestMapping("/eventos/{eventoId}/convidados")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
@Tag(name = "4. Convidados", description = "Gerenciamento da lista de convidados e acompanhantes de um evento")
public class ConvidadoController {

    private final ConvidadoService convidadoService;

    @PostMapping
    @Operation(
            summary = "Adicionar convidado ao evento",
            description = "Cadastra um convidado na lista do evento. Valida unicidade do e-mail no evento (RN-007) e a capacidade máxima disponível incluindo acompanhantes (RN-008)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Convidado cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, evento cancelado ou capacidade máxima do evento excedida (RN-008)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para gerenciar convidados deste evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Convidado com este e-mail já está cadastrado no evento (RN-007)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConvidadoResponseDTO> criar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @RequestBody @Valid CriarConvidadoDTO dto) {
        ConvidadoResponseDTO response = convidadoService.criar(eventoId, dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar convidados do evento",
            description = "Retorna a lista paginada de convidados de um evento, permitindo busca opcional por nome ou e-mail."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de convidados retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Page<ConvidadoResponseDTO>> listar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "Termo opcional para filtrar convidados por nome ou e-mail", example = "Maria")
            @RequestParam(required = false) String busca,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<ConvidadoResponseDTO> response = convidadoService.listar(eventoId, busca, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar convidado por ID",
            description = "Retorna os dados de um convidado específico pertencente ao evento informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convidado encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou convidado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConvidadoResponseDTO> buscarPorId(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convidado", example = "770e8400-e29b-41d4-a716-446655440002")
            @PathVariable UUID id) {
        ConvidadoResponseDTO response = convidadoService.buscarPorId(eventoId, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar dados do convidado",
            description = "Atualiza nome, e-mail, telefone e limite de acompanhantes do convidado, respeitando a capacidade do evento (RN-008)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convidado atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, evento cancelado ou capacidade máxima do evento excedida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para gerenciar convidados deste evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou convidado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Novo e-mail informado já pertence a outro convidado no mesmo evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ConvidadoResponseDTO> atualizar(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convidado", example = "770e8400-e29b-41d4-a716-446655440002")
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarConvidadoDTO dto) {
        ConvidadoResponseDTO response = convidadoService.atualizar(eventoId, id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remover convidado do evento",
            description = "Remove o convidado da lista do evento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Convidado removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Operação não permitida em evento cancelado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para gerenciar convidados deste evento",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou convidado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID eventoId,
            @Parameter(description = "ID único do convidado", example = "770e8400-e29b-41d4-a716-446655440002")
            @PathVariable UUID id) {
        convidadoService.remover(eventoId, id);
        return ResponseEntity.noContent().build();
    }
}


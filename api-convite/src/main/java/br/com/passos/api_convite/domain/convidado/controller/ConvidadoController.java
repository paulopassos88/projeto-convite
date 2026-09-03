package br.com.passos.api_convite.domain.convidado.controller;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.dto.ConvidadoResponseDTO;
import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.service.ConvidadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ConvidadoController {

    private final ConvidadoService convidadoService;

    @PostMapping
    public ResponseEntity<ConvidadoResponseDTO> criar(
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
    public ResponseEntity<Page<ConvidadoResponseDTO>> listar(
            @PathVariable UUID eventoId,
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<ConvidadoResponseDTO> response = convidadoService.listar(eventoId, busca, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvidadoResponseDTO> buscarPorId(
            @PathVariable UUID eventoId,
            @PathVariable UUID id) {
        ConvidadoResponseDTO response = convidadoService.buscarPorId(eventoId, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConvidadoResponseDTO> atualizar(
            @PathVariable UUID eventoId,
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarConvidadoDTO dto) {
        ConvidadoResponseDTO response = convidadoService.atualizar(eventoId, id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable UUID eventoId,
            @PathVariable UUID id) {
        convidadoService.remover(eventoId, id);
        return ResponseEntity.noContent().build();
    }
}

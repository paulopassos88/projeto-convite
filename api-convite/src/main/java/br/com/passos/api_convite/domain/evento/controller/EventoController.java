package br.com.passos.api_convite.domain.evento.controller;

import br.com.passos.api_convite.domain.evento.dto.AtualizarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.EventoResponseDTO;
import br.com.passos.api_convite.domain.evento.service.EventoService;
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
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
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
    public ResponseEntity<Page<EventoResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "dataInicio") Pageable pageable) {
        Page<EventoResponseDTO> response = eventoService.listar(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public ResponseEntity<EventoResponseDTO> buscarPorId(@PathVariable UUID id) {
        EventoResponseDTO response = eventoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public ResponseEntity<EventoResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarEventoDTO dto) {
        EventoResponseDTO response = eventoService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        eventoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}

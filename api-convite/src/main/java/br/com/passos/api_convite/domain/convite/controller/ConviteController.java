package br.com.passos.api_convite.domain.convite.controller;

import br.com.passos.api_convite.domain.convite.dto.ConviteResponseDTO;
import br.com.passos.api_convite.domain.convite.dto.MensagemSucessoDTO;
import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.service.ConviteService;
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
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
public class ConviteController {

    private final ConviteService conviteService;

    @PostMapping("/eventos/{eventoId}/convidados/{convidadoId}/convite")
    public ResponseEntity<ConviteResponseDTO> gerar(
            @PathVariable UUID eventoId,
            @PathVariable UUID convidadoId) {
        ConviteResponseDTO response = conviteService.gerar(eventoId, convidadoId);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/eventos/{eventoId}/convites")
    public ResponseEntity<Page<ConviteResponseDTO>> listar(
            @PathVariable UUID eventoId,
            @RequestParam(required = false) StatusConvite status,
            @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable) {
        Page<ConviteResponseDTO> response = conviteService.listar(eventoId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eventos/{eventoId}/convites/{id}")
    public ResponseEntity<ConviteResponseDTO> buscarPorId(
            @PathVariable UUID eventoId,
            @PathVariable UUID id) {
        ConviteResponseDTO response = conviteService.buscarPorId(eventoId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eventos/{eventoId}/convidados/{convidadoId}/convite")
    public ResponseEntity<ConviteResponseDTO> buscarPorConvidadoId(
            @PathVariable UUID eventoId,
            @PathVariable UUID convidadoId) {
        ConviteResponseDTO response = conviteService.buscarPorConvidadoId(eventoId, convidadoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convites/{id}/enviar")
    public ResponseEntity<MensagemSucessoDTO> enviar(@PathVariable UUID id) {
        MensagemSucessoDTO response = conviteService.solicitarEnvio(id);
        return ResponseEntity.ok(response);
    }
}

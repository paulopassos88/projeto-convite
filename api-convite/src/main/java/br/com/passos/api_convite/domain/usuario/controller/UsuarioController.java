package br.com.passos.api_convite.domain.usuario.controller;

import br.com.passos.api_convite.domain.usuario.dto.CadastroUsuarioDTO;
import br.com.passos.api_convite.domain.usuario.dto.UsuarioResponseDTO;
import br.com.passos.api_convite.domain.usuario.service.CadastroUsuarioService;
import br.com.passos.api_convite.infra.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "2. Usuários", description = "Gestão e cadastro de usuários do sistema (exclusivo para ADMIN)")
public class UsuarioController {

    private final CadastroUsuarioService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Permite a um usuário ADMIN cadastrar novos administradores, organizadores ou controladores de portaria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos campos informados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer perfil ADMIN)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid CadastroUsuarioDTO dto) {
        UsuarioResponseDTO response = service.cadastrar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }
}

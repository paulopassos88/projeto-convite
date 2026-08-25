package br.com.passos.api_convite.domain.usuario.service;

import br.com.passos.api_convite.domain.usuario.dto.CadastroUsuarioDTO;
import br.com.passos.api_convite.domain.usuario.dto.UsuarioResponseDTO;
import br.com.passos.api_convite.domain.usuario.mapper.UsuarioMapper;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import br.com.passos.api_convite.domain.usuario.repository.UsuarioRepository;
import br.com.passos.api_convite.domain.usuario.service.exceptions.EmailJaCadastradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CadastroUsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO cadastrar(CadastroUsuarioDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new EmailJaCadastradoException("O e-mail " + dto.email() + " já está em uso.");
        }

        Usuario novoUsuario = mapper.toEntity(dto);
        novoUsuario.setSenhaHash(passwordEncoder.encode(dto.senha()));

        Usuario usuarioSalvo = repository.save(novoUsuario);

        return mapper.toResponse(usuarioSalvo);
    }
}

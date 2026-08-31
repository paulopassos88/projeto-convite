package br.com.passos.api_convite.domain.evento.service;

import br.com.passos.api_convite.domain.evento.dto.AtualizarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.EventoResponseDTO;
import br.com.passos.api_convite.domain.evento.mapper.EventoMapper;
import br.com.passos.api_convite.domain.evento.model.Evento;
import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import br.com.passos.api_convite.domain.evento.repository.EventoRepository;
import br.com.passos.api_convite.domain.evento.service.exceptions.EventoNaoEncontradoException;
import br.com.passos.api_convite.domain.evento.service.validacoes.ValidadorCriacaoEvento;
import br.com.passos.api_convite.domain.usuario.model.Perfil;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import br.com.passos.api_convite.domain.usuario.service.exceptions.BusinessException;
import br.com.passos.api_convite.infra.security.UsuarioLogadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioLogadoService usuarioLogadoService;
    private final EventoMapper eventoMapper;
    private final List<ValidadorCriacaoEvento> validadoresCriacao;

    @Transactional
    public EventoResponseDTO criar(CriarEventoDTO dto) {
        validadoresCriacao.forEach(v -> v.validar(dto));

        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado()
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));

        Evento evento = eventoMapper.toEntity(dto);
        evento.setOrganizador(usuarioLogado);
        evento.setStatus(StatusEvento.ATIVO);

        eventoRepository.save(evento);
        return eventoMapper.toResponseDTO(evento);
    }

    @Transactional(readOnly = true)
    public Page<EventoResponseDTO> listar(Pageable pageable) {
        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado()
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));

        if (usuarioLogado.getPerfil() == Perfil.ADMIN) {
            return eventoRepository.findAll(pageable).map(eventoMapper::toResponseDTO);
        }

        return eventoRepository.findAllByOrganizadorId(usuarioLogado.getId(), pageable)
                .map(eventoMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO buscarPorId(UUID id) {
        Evento evento = buscarEventoPorIdComValidacaoAcesso(id);
        return eventoMapper.toResponseDTO(evento);
    }

    @Transactional
    public EventoResponseDTO atualizar(UUID id, AtualizarEventoDTO dto) {
        Evento evento = buscarEventoPorIdComValidacaoAcesso(id);

        if (evento.getStatus() == StatusEvento.ENCERRADO || evento.getStatus() == StatusEvento.CANCELADO) {
            throw new BusinessException("Não é possível alterar um evento encerrado ou cancelado.");
        }

        eventoMapper.updateEntityFromDTO(dto, evento);
        return eventoMapper.toResponseDTO(evento);
    }

    @Transactional
    public void cancelar(UUID id) {
        Evento evento = buscarEventoPorIdComValidacaoAcesso(id);
        evento.setStatus(StatusEvento.CANCELADO);
    }

    private Evento buscarEventoPorIdComValidacaoAcesso(UUID id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado para o ID: " + id));

        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado()
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));

        boolean ehAdmin = usuarioLogado.getPerfil() == Perfil.ADMIN;
        boolean ehDono = evento.getOrganizador().getId().equals(usuarioLogado.getId());

        if (!ehAdmin && !ehDono) {
            throw new AccessDeniedException("Você não tem permissão para acessar ou modificar este evento.");
        }

        return evento;
    }
}

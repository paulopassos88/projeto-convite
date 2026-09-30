package br.com.passos.api_convite.domain.convidado.service;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.dto.ConvidadoResponseDTO;
import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.mapper.ConvidadoMapper;
import br.com.passos.api_convite.domain.convidado.model.Convidado;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.convidado.service.exceptions.ConvidadoNaoEncontradoException;
import br.com.passos.api_convite.domain.convidado.service.validacoes.ValidadorAtualizacaoConvidado;
import br.com.passos.api_convite.domain.convidado.service.validacoes.ValidadorCriacaoConvidado;
import br.com.passos.api_convite.domain.evento.model.Evento;
import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import br.com.passos.api_convite.domain.evento.repository.EventoRepository;
import br.com.passos.api_convite.domain.evento.service.exceptions.EventoNaoEncontradoException;
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
public class ConvidadoService {

    private final ConvidadoRepository convidadoRepository;
    private final EventoRepository eventoRepository;
    private final ConvidadoMapper convidadoMapper;
    private final UsuarioLogadoService usuarioLogadoService;
    private final List<ValidadorCriacaoConvidado> validadoresCriacao;
    private final List<ValidadorAtualizacaoConvidado> validadoresAtualizacao;

    @Transactional
    public ConvidadoResponseDTO criar(UUID eventoId, CriarConvidadoDTO dto) {
        Evento evento = buscarEventoComValidacaoAcesso(eventoId);
        validarEventoAtivo(evento);

        validadoresCriacao.forEach(v -> v.validar(eventoId, dto));
        validarNumeroDeAcompanhantes(evento, dto.acompanhantesPermitidos());

        Convidado convidado = convidadoMapper.toEntity(dto);
        convidado.setEvento(evento);

        convidadoRepository.save(convidado);
        return convidadoMapper.toResponseDTO(convidado);
    }

    @Transactional(readOnly = true)
    public Page<ConvidadoResponseDTO> listar(UUID eventoId, String busca, Pageable pageable) {
        buscarEventoComValidacaoAcesso(eventoId);

        if (busca != null && !busca.isBlank()) {
            return convidadoRepository.findAllByEventoIdAndBusca(eventoId, busca.trim(), pageable)
                    .map(convidadoMapper::toResponseDTO);
        }

        return convidadoRepository.findAllByEventoId(eventoId, pageable)
                .map(convidadoMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ConvidadoResponseDTO buscarPorId(UUID eventoId, UUID convidadoId) {
        buscarEventoComValidacaoAcesso(eventoId);
        Convidado convidado = buscarConvidadoPorIdEEvento(convidadoId, eventoId);
        return convidadoMapper.toResponseDTO(convidado);
    }

    @Transactional
    public ConvidadoResponseDTO atualizar(UUID eventoId, UUID convidadoId, AtualizarConvidadoDTO dto) {
        Evento evento = buscarEventoComValidacaoAcesso(eventoId);
        validarEventoAtivo(evento);

        Convidado convidado = buscarConvidadoPorIdEEvento(convidadoId, eventoId);

        validadoresAtualizacao.forEach(v -> v.validar(eventoId, convidadoId, dto));
        validarNumeroDeAcompanhantes(evento, dto.acompanhantesPermitidos());

        convidadoMapper.updateEntityFromDTO(dto, convidado);
        return convidadoMapper.toResponseDTO(convidado);
    }

    @Transactional
    public void remover(UUID eventoId, UUID convidadoId) {
        Evento evento = buscarEventoComValidacaoAcesso(eventoId);
        validarEventoAtivo(evento);

        Convidado convidado = buscarConvidadoPorIdEEvento(convidadoId, eventoId);
        convidadoRepository.delete(convidado);
    }

    private Evento buscarEventoComValidacaoAcesso(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado para o ID: " + eventoId));

        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado()
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));

        boolean ehAdmin = usuarioLogado.getPerfil() == Perfil.ADMIN;
        boolean ehDono = evento.getOrganizador().getId().equals(usuarioLogado.getId());

        if (!ehAdmin && !ehDono) {
            throw new AccessDeniedException("Você não tem permissão para acessar ou gerenciar convidados deste evento.");
        }

        return evento;
    }

    private Convidado buscarConvidadoPorIdEEvento(UUID convidadoId, UUID eventoId) {
        return convidadoRepository.findByIdAndEventoId(convidadoId, eventoId)
                .orElseThrow(() -> new ConvidadoNaoEncontradoException("Convidado não encontrado para o ID: " + convidadoId));
    }

    private void validarEventoAtivo(Evento evento) {
        if (evento.getStatus() == StatusEvento.ENCERRADO || evento.getStatus() == StatusEvento.CANCELADO) {
            throw new BusinessException("Não é possível realizar operações em convidados de um evento encerrado ou cancelado.");
        }
    }

    private void validarNumeroDeAcompanhantes(Evento evento, Integer acompanhantesPermitidos) {
        if (acompanhantesPermitidos != null && evento.getAcompanhantesPadrao() != null
                && acompanhantesPermitidos > evento.getAcompanhantesPadrao()) {
            throw new BusinessException("Número máximo de acompanhantes atingido para este evento.");
        }
    }
}

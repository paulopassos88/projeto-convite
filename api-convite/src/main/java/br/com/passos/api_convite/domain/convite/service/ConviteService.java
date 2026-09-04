package br.com.passos.api_convite.domain.convite.service;

import br.com.passos.api_convite.domain.convidado.model.Convidado;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.convidado.service.exceptions.ConvidadoNaoEncontradoException;
import br.com.passos.api_convite.domain.convite.dto.ConviteResponseDTO;
import br.com.passos.api_convite.domain.convite.mapper.ConviteMapper;
import br.com.passos.api_convite.domain.convite.model.Convite;
import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.model.StatusEnvio;
import br.com.passos.api_convite.domain.convite.repository.ConviteRepository;
import br.com.passos.api_convite.domain.convite.service.exceptions.ConviteJaGeradoException;
import br.com.passos.api_convite.domain.convite.service.exceptions.ConviteNaoEncontradoException;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConviteService {

    private final ConviteRepository conviteRepository;
    private final ConvidadoRepository convidadoRepository;
    private final EventoRepository eventoRepository;
    private final ConviteMapper conviteMapper;
    private final GeradorCodigoConviteService geradorCodigoConviteService;
    private final UsuarioLogadoService usuarioLogadoService;

    @Transactional
    public ConviteResponseDTO gerar(UUID eventoId, UUID convidadoId) {
        Evento evento = buscarEventoComValidacaoAcesso(eventoId);
        validarEventoAtivo(evento);

        Convidado convidado = buscarConvidadoPorIdEEvento(convidadoId, eventoId);

        if (conviteRepository.existsByConvidadoIdAndEventoId(convidadoId, eventoId)) {
            throw new ConviteJaGeradoException("Já existe um convite gerado para este convidado no evento informado.");
        }

        String codigo = geradorCodigoConviteService.gerarCodigoUnico();

        Convite convite = new Convite();
        convite.setEvento(evento);
        convite.setConvidado(convidado);
        convite.setCodigo(codigo);
        convite.setStatus(StatusConvite.PENDENTE);
        convite.setStatusEnvio(StatusEnvio.NAO_ENVIADO);

        conviteRepository.save(convite);
        return conviteMapper.toResponseDTO(convite);
    }

    @Transactional(readOnly = true)
    public Page<ConviteResponseDTO> listar(UUID eventoId, StatusConvite status, Pageable pageable) {
        buscarEventoComValidacaoAcesso(eventoId);

        if (status != null) {
            return conviteRepository.findAllByEventoIdAndStatus(eventoId, status, pageable)
                    .map(conviteMapper::toResponseDTO);
        }

        return conviteRepository.findAllByEventoId(eventoId, pageable)
                .map(conviteMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ConviteResponseDTO buscarPorId(UUID eventoId, UUID id) {
        buscarEventoComValidacaoAcesso(eventoId);
        Convite convite = conviteRepository.findByIdAndEventoId(id, eventoId)
                .orElseThrow(() -> new ConviteNaoEncontradoException("Convite não encontrado para o ID: " + id));
        return conviteMapper.toResponseDTO(convite);
    }

    @Transactional(readOnly = true)
    public ConviteResponseDTO buscarPorConvidadoId(UUID eventoId, UUID convidadoId) {
        buscarEventoComValidacaoAcesso(eventoId);
        Convite convite = conviteRepository.findByConvidadoIdAndEventoId(convidadoId, eventoId)
                .orElseThrow(() -> new ConviteNaoEncontradoException("Convite não encontrado para o convidado informado."));
        return conviteMapper.toResponseDTO(convite);
    }

    private Evento buscarEventoComValidacaoAcesso(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EventoNaoEncontradoException("Evento não encontrado para o ID: " + eventoId));

        Usuario usuarioLogado = usuarioLogadoService.getUsuarioLogado()
                .orElseThrow(() -> new IllegalStateException("Usuário não autenticado"));

        boolean ehAdmin = usuarioLogado.getPerfil() == Perfil.ADMIN;
        boolean ehDono = evento.getOrganizador().getId().equals(usuarioLogado.getId());

        if (!ehAdmin && !ehDono) {
            throw new AccessDeniedException("Você não tem permissão para acessar ou gerenciar convites deste evento.");
        }

        return evento;
    }

    private Convidado buscarConvidadoPorIdEEvento(UUID convidadoId, UUID eventoId) {
        return convidadoRepository.findByIdAndEventoId(convidadoId, eventoId)
                .orElseThrow(() -> new ConvidadoNaoEncontradoException("Convidado não encontrado para o ID: " + convidadoId));
    }

    private void validarEventoAtivo(Evento evento) {
        if (evento.getStatus() == StatusEvento.ENCERRADO || evento.getStatus() == StatusEvento.CANCELADO) {
            throw new BusinessException("Não é possível realizar operações de convite em um evento encerrado ou cancelado.");
        }
    }
}

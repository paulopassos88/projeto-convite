package br.com.passos.api_convite.domain.convidado.service.validacoes;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.convidado.service.exceptions.EmailConvidadoDuplicadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidadorEmailDuplicadoAtualizacaoConvidado implements ValidadorAtualizacaoConvidado {

    private final ConvidadoRepository convidadoRepository;

    @Override
    public void validar(UUID eventoId, UUID convidadoId, AtualizarConvidadoDTO dto) {
        if (convidadoRepository.existsByEventoIdAndEmailIgnoreCaseAndIdNot(eventoId, dto.email(), convidadoId)) {
            throw new EmailConvidadoDuplicadoException("Já existe outro convidado cadastrado com o e-mail informado para este evento.");
        }
    }
}

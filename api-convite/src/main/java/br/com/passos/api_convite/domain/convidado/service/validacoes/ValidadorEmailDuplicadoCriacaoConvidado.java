package br.com.passos.api_convite.domain.convidado.service.validacoes;

import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.convidado.service.exceptions.EmailConvidadoDuplicadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidadorEmailDuplicadoCriacaoConvidado implements ValidadorCriacaoConvidado {

    private final ConvidadoRepository convidadoRepository;

    @Override
    public void validar(UUID eventoId, CriarConvidadoDTO dto) {
        if (convidadoRepository.existsByEventoIdAndEmailIgnoreCase(eventoId, dto.email())) {
            throw new EmailConvidadoDuplicadoException("Já existe um convidado cadastrado com o e-mail informado para este evento.");
        }
    }
}

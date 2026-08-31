package br.com.passos.api_convite.domain.evento.service.validacoes;

import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.service.exceptions.ValidacaoEventoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDataTerminoPosteriorInicio implements ValidadorCriacaoEvento {

    @Override
    public void validar(CriarEventoDTO dto) {
        if (dto.dataInicio() != null && dto.dataTermino() != null && !dto.dataTermino().isAfter(dto.dataInicio())) {
            throw new ValidacaoEventoException("A data e hora de término deve ser posterior à data e hora de início.");
        }
    }
}

package br.com.passos.api_convite.domain.evento.service.validacoes;

import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.service.exceptions.ValidacaoEventoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorDataInicioFutura implements ValidadorCriacaoEvento {

    @Override
    public void validar(CriarEventoDTO dto) {
        if (dto.dataInicio() != null && dto.dataInicio().isBefore(LocalDateTime.now())) {
            throw new ValidacaoEventoException("A data de início do evento não pode estar no passado.");
        }
    }
}

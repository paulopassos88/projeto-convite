package br.com.passos.api_convite.domain.evento.service.validacoes;

import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;

public interface ValidadorCriacaoEvento {

    void validar(CriarEventoDTO dto);

}

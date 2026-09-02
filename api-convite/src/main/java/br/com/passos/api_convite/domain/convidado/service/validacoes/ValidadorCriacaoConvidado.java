package br.com.passos.api_convite.domain.convidado.service.validacoes;

import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;

import java.util.UUID;

public interface ValidadorCriacaoConvidado {

    void validar(UUID eventoId, CriarConvidadoDTO dto);
}

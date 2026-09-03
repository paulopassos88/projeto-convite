package br.com.passos.api_convite.domain.convidado.service.validacoes;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;

import java.util.UUID;

public interface ValidadorAtualizacaoConvidado {

    void validar(UUID eventoId, UUID convidadoId, AtualizarConvidadoDTO dto);
}

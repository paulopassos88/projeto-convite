package br.com.passos.api_convite.domain.evento.service.exceptions;

import br.com.passos.api_convite.domain.usuario.service.exceptions.BusinessException;

public class ValidacaoEventoException extends BusinessException {

    public ValidacaoEventoException(String message) {
        super(message);
    }
}

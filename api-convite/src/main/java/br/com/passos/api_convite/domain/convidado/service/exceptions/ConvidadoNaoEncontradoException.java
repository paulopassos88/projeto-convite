package br.com.passos.api_convite.domain.convidado.service.exceptions;

public class ConvidadoNaoEncontradoException extends RuntimeException {
    public ConvidadoNaoEncontradoException(String message) {
        super(message);
    }
}

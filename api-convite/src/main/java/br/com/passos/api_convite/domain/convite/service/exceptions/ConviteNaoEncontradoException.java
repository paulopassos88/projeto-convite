package br.com.passos.api_convite.domain.convite.service.exceptions;

public class ConviteNaoEncontradoException extends RuntimeException {
    public ConviteNaoEncontradoException(String message) {
        super(message);
    }
}

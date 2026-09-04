package br.com.passos.api_convite.domain.convite.service.exceptions;

public class ConviteJaGeradoException extends RuntimeException {
    public ConviteJaGeradoException(String message) {
        super(message);
    }
}

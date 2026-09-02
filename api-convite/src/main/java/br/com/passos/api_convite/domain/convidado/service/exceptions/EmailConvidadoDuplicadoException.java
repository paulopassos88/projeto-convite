package br.com.passos.api_convite.domain.convidado.service.exceptions;

public class EmailConvidadoDuplicadoException extends RuntimeException {
    public EmailConvidadoDuplicadoException(String message) {
        super(message);
    }
}

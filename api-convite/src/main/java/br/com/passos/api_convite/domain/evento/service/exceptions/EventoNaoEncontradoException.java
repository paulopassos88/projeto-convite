package br.com.passos.api_convite.domain.evento.service.exceptions;

public class EventoNaoEncontradoException extends RuntimeException {

    public EventoNaoEncontradoException(String message) {
        super(message);
    }
}

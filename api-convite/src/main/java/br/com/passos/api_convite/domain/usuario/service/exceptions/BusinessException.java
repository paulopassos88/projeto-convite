package br.com.passos.api_convite.domain.usuario.service.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

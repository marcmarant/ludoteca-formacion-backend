package com.ccsw.tutorial.common.exception;

/**
 * Excepción para manejar el acceso a metodos donde el usuario no esta autenticado y el metodo lo requiere
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}


package com.ccsw.tutorial.common.exception;

/**
 * Excepción para manejar el intento de prestamo de un juego que ya se encuentra prestado en ese periodo
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}


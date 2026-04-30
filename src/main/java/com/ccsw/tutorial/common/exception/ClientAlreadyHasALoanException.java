package com.ccsw.tutorial.common.exception;

/**
 * Excepción para manejar el conflicto a la hora de crear un préstamo para un cliente en un intervalo de tiempo
 * en donde coincida que el cliente ya tenga otro préstamo.
 */
public class ClientAlreadyHasALoanException extends RuntimeException {
    public ClientAlreadyHasALoanException(String message) {
        super(message);
    }
}

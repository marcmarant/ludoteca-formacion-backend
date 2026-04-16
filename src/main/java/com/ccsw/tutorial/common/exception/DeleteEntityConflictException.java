package com.ccsw.tutorial.common.exception;

/**
 * Excepción para manejar el conflicto a la hora de borrar una entidad que esta asociada a otra mediante FK
 */
public class DeleteEntityConflictException extends RuntimeException {
    public DeleteEntityConflictException(String message) {
        super(message);
    }
}

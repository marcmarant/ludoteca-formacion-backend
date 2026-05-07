package com.ccsw.tutorial.common.exception;

/**
 * Excepción para manejar el conflicto a la hora de intentar borrar o cambiar el rol de un usuario administrador
 * cuando no exista ningun otro usuario con rol de administrador.
 */
public class AtLeastOneAdministratorRequiredException extends RuntimeException {
    public AtLeastOneAdministratorRequiredException(String message) {
        super(message);
    }
}

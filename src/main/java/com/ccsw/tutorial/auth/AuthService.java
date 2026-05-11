package com.ccsw.tutorial.auth;

/**
 * Interfaz que define los servicios de autentificacion.
 * Permite autenticar un usuario.
 *
 * @author Marcos Martínez Antón
 */
public interface AuthService {
    String auth(AuthDTO request);
}

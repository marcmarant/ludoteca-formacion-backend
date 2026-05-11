package com.ccsw.tutorial.user.model;

/**
 * Enum que define los diferentes roles que puede tener un usuario autenticado en el sistema.
 * Actualmente existe el rol de empleado, y el de administrador.
 * Pudiendo solo los administradores dar de alta o baja y cambiar el rol de usuarios,
 * y pudiendo los dos hacer el resto de operaciones del sistema.
 *
 * @author Marcos Martínez Antón
 */
public enum Role {
    ROLE_EMPLOYEE,
    ROLE_ADMIN
}

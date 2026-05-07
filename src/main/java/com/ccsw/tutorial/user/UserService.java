package com.ccsw.tutorial.user;


import com.ccsw.tutorial.common.exception.AtLeastOneAdministratorRequiredException;
import com.ccsw.tutorial.user.model.CreateUserDTO;
import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Recupera todos los usuarios
     *
     * @return {@link List} de {@link User}
     */
    List<User> findAll();

    /**
     * Recupera un usuario por Id
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no se encuentra ningun usuario
     * @return {@link User}
     */
    User findById(Long id) throws EntityNotFoundException;

    /**
     * Crea un nuevo usuario
     *
     * @param dto datos de la entidad
     */
    void create(CreateUserDTO dto);

    /**
     * Actualiza una categoría existente
     *
     * @param newRole nuevo rol a asignar al usuario
     * @throws EntityNotFoundException si no existe el usuario a actualizar
     * @throws AtLeastOneAdministratorRequiredException si se intenta cambiar el rol de un administrador sin haber otro
     */
    void updateRole(Long id, Role newRole) throws EntityNotFoundException;

    /**
     * Elimina un usuario
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el usuario a borrar
     * @throws AtLeastOneAdministratorRequiredException si se intenta borrar el rol de un administrador sin haber otro
     */
    void delete(Long id) throws EntityNotFoundException;
}

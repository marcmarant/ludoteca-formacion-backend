package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link User}
 *
 * @author Marcos Martínez Antón
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Recupera un usuario por su username
     *
     * @param username del usuario a recuperar
     * @return {@link User}
     */
    Optional<User> findByUsername(String username);

    /**
     * Devuleve si existen usuarios con un rol determinado y un id diferente al proporcionado
     *
     * @param role rol del que buscar usuarios
     * @param id  id del usuario a excluir de la búsqueda
     * @return true si existen usuarios con el rol especificado y un id diferente al proporcionado, false en caso contrario
     */
    boolean existsByRoleAndIdNot(Role role, Long id);
}

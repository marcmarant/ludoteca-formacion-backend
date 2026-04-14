package com.ccsw.tutorial.user;

import com.ccsw.tutorial.common.exception.UnauthorizedException;
import com.ccsw.tutorial.user.model.User;

public interface UserService {

    /**
     * Recupera un usuario por Id
     *
     * @param id PK de la entidad
     * @throws UnauthorizedException si no se encuentra ningun usuario
     * @return {@link User}
     */
    User findById(Long id) throws UnauthorizedException;
}

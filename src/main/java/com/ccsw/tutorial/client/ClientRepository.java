package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    /**
     * Devuelve si existe un {@link Client} con el mismo nombre que el proporcionado
     *
     * @param name nombre del cliente
     * @return true si existe un cliente con el mismo nombre, false en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Devuelve si existe un {@link Client} con el mismo nombre que el proporcionado
     * pero con un id diferente al indicado (Excluyendo el mismo registro)
     *
     * @param name nombre del cliente
     * @param id id del cliente para excluirlo
     * @return true si existe un cliente con el mismo nombre, false en caso contrario
     */
    boolean existsByNameAndIdNot(String name, Long id);
}

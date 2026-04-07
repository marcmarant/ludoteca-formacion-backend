package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.ClientDTO;
import com.ccsw.tutorial.client.model.Client;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    /**
     * Recuperar todos los clientes
     *
     * @return {@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Crea un nuevo cliente
     *
     * @param dto datos de la entidad
     * @throws IllegalArgumentException si el nombre ya existe
     */
    void create(ClientDTO dto) throws IllegalArgumentException;

    /**
     * Actualiza un cliente existente
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws IllegalArgumentException si el nombre ya existe
     * @throws EntityNotFoundException si no existe el cliente a actualizar
     */
    void update(ClientDTO dto) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Elimina un cliente
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el cliente a eliminar
     */
    void delete(Long id) throws EntityNotFoundException;

}

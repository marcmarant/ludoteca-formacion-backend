package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.ClientDTO;
import com.ccsw.tutorial.common.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIT extends AbstractIT {

    public static final String SERVICE_PATH = "/clients";

    // Basado en java/resources/data.sql
    private static final int TOTAL_CLIENTS = 3;
    private static final Long LOAN_REFERENCED_CLIENT_ID = 1L;
    private static final Long NOT_REFERENCED_CLIENT_ID = 3L;
    private static final Long NOT_EXISTS_CLIENT_ID = 0L;
    private static final Long CLIENT_TO_UPDATE_ID = 2L;

    private final ParameterizedTypeReference<List<ClientDTO>> listResponseType = new ParameterizedTypeReference<>() {};

    @Test
    public void findAllShouldReturnAllClients() {

        ResponseEntity<List<ClientDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_CLIENTS, response.getBody().size());
    }

    @Test
    public void createShouldCreateANewClient() {
        ClientDTO dto = new ClientDTO();
        dto.setName("New Client");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals("New Client", findAllResponse.getBody().get(TOTAL_CLIENTS).getName());
    }

    @Test
    public void createWithAnEmptyNameShouldReturnAnError() {
        ClientDTO dto = new ClientDTO();
        dto.setName("");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals(TOTAL_CLIENTS, findAllResponse.getBody().size());
    }

    @Test
    public void createWithANullNameShouldReturnAnError() {
        ClientDTO dto = new ClientDTO();
        dto.setName(null);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals(TOTAL_CLIENTS, findAllResponse.getBody().size());
    }

    @Test
    public void createWithAnAlreadyExistingNameShouldReturnAnError() {
        ClientDTO dto = new ClientDTO();
        dto.setName("Marcos Martinez");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals(TOTAL_CLIENTS, findAllResponse.getBody().size());
    }

    @Test
    public void updateShouldReplaceAnExistingClient() {
        ClientDTO dto = new ClientDTO();
        dto.setId(CLIENT_TO_UPDATE_ID);
        dto.setName("Updated Client");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + CLIENT_TO_UPDATE_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals("Updated Client", findAllResponse.getBody().get(1).getName()); // 1 es la posición de CLIENT_TO_UPDATE_ID
    }

    @Test
    public void updateWithNonExistentIdShouldReturnANotFoundError() {
        ClientDTO dto = new ClientDTO();
        dto.setId(NOT_EXISTS_CLIENT_ID);
        dto.setName("Updated Client");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CLIENT_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateWithAnExistentNameShouldReturnAnError() {
        ClientDTO dto = new ClientDTO();
        dto.setId(CLIENT_TO_UPDATE_ID);
        dto.setName("Marcos Martinez");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + CLIENT_TO_UPDATE_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteAClient() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_CLIENT_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<List<ClientDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(TOTAL_CLIENTS - 1, findAllResponse.getBody().size());
    }

    @Test
    public void deleteWithNonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CLIENT_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteReferencedClientShouldReturnConflictError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + LOAN_REFERENCED_CLIENT_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}

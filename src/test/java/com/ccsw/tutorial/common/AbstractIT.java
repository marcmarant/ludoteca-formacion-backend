package com.ccsw.tutorial.common;

import com.ccsw.tutorial.auth.AuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Clase abstracta para los tests de integración que define el puerto y el TestRestTemplate,
 * además de permitir obtener un token de autenticación para las pruebas que lo requieran.
 */
public abstract class AbstractIT {

    protected static final String LOCALHOST = "http://localhost:";
    protected static final String AUTH_PATH = "/auth";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    private String getAuthToken() {
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername("admin");
        authDTO.setPassword("admin");

        ResponseEntity<String> response = restTemplate.exchange(
                LOCALHOST + port + AUTH_PATH,
                HttpMethod.POST,
                new HttpEntity<>(authDTO),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        return response.getBody();
    }

    protected <T> HttpEntity<T> buildAuthEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAuthToken());
        return new HttpEntity<>(body, headers);
    }

    protected HttpEntity<?> buildAuthEntity() {
        return buildAuthEntity(null);
    }
}

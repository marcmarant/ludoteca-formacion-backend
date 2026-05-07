package com.ccsw.tutorial.auth;

import com.ccsw.tutorial.common.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthIT extends AbstractIT {

    public static final String SERVICE_PATH = "/auth";

    @Test
    public void LoginWithCorrectCredentialsShouldReturnAuthUser() {

        AuthDTO dto = new AuthDTO();
        dto.setUsername("admin");
        dto.setPassword("admin");

        ResponseEntity<String> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isBlank());
    }

    @Test
    public void LoginWithIncorrectCredentialsShouldReturnUnauthorizedError() {

        AuthDTO dto = new AuthDTO();
        dto.setUsername("incorrect_username");
        dto.setPassword("incorrect_password");

        ResponseEntity<String> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
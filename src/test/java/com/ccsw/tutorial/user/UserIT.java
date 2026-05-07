package com.ccsw.tutorial.user;

import com.ccsw.tutorial.common.AbstractIT;
import com.ccsw.tutorial.user.model.CreateUserDTO;
import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserIT extends AbstractIT {

    public static final String SERVICE_PATH = "/users";

    // Basado en java/resources/data.sql
    private static final int TOTAL_USERS = 2;
    private static final Long EXISTS_ADMIN_USER_ID = 1L;
    private static final Long EXISTS_EMPLOYEE_USER_ID = 2L;
    private static final Long NOT_EXISTS_USER_ID = 0L;
    private static final String NEW_USERNAME = "employee2";

    private final ParameterizedTypeReference<List<UserDTO>> listResponseType = new ParameterizedTypeReference<>() {};
    private final ParameterizedTypeReference<UserDTO> userResponseType = new ParameterizedTypeReference<>() {};

    @Test
    public void findAllShouldReturnAllUsers() {

        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                buildAuthAdminEntity(),
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_USERS, response.getBody().size());
    }

    @Test
    public void findAllWithoutAdminTokenShouldReturnForbidden() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void findByIdShouldReturnSpecificUser() {

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_ADMIN_USER_ID,
                HttpMethod.GET,
                null,
                userResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXISTS_ADMIN_USER_ID, response.getBody().getId());
        assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    public void findByANonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_USER_ID,
                HttpMethod.GET,
                null,
                userResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void createShouldCreateANewUser() {

        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername(NEW_USERNAME);
        dto.setPassword("employee2");
        dto.setRole(Role.ROLE_ADMIN);

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthAdminEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<UserDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                buildAuthAdminEntity(dto),
                listResponseType
        );

        assertNotNull(findAllResponse);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertNotNull(findAllResponse.getBody());
        assertEquals(TOTAL_USERS + 1, findAllResponse.getBody().size());
        assertEquals(NEW_USERNAME, findAllResponse.getBody().getLast().getUsername());
    }

    @Test
    public void updateShouldReplaceExistingUserRole() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_EMPLOYEE_USER_ID + "?role=" + Role.ROLE_ADMIN,
                HttpMethod.PATCH,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<UserDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_EMPLOYEE_USER_ID,
                HttpMethod.GET,
                null,
                userResponseType
        );

        assertNotNull(findByIdResponse);
        assertEquals(HttpStatus.OK, findByIdResponse.getStatusCode());
        assertNotNull(findByIdResponse.getBody());
        assertEquals(Role.ROLE_ADMIN, findByIdResponse.getBody().getRole());
    }

    @Test
    public void updateWithNonExistentIdShouldReturnANotFoundError() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_USER_ID + "?role=" + Role.ROLE_EMPLOYEE,
                HttpMethod.PATCH,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateAdminRoleToEmployeeShouldReturnConflictError() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_ADMIN_USER_ID + "?role=" + Role.ROLE_EMPLOYEE,
                HttpMethod.PATCH,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteExpectedUser() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_EMPLOYEE_USER_ID,
                HttpMethod.DELETE,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<UserDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_EMPLOYEE_USER_ID,
                HttpMethod.GET,
                null,
                userResponseType
        );

        assertNotNull(findByIdResponse);
        assertEquals(HttpStatus.NOT_FOUND, findByIdResponse.getStatusCode());
    }

    @Test
    public void deleteWithNonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_USER_ID,
                HttpMethod.DELETE,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteLastAdminUserShouldReturnConflictError() {

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_ADMIN_USER_ID,
                HttpMethod.DELETE,
                buildAuthAdminEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
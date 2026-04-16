package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.AbstractIT;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorIT extends AbstractIT {

    public static final String SERVICE_PATH = "/authors";

    // Based on java/resources/data.sql
    private static final int TOTAL_AUTHORS = 6;
    public static final Long GAME_REFERENCED_AUTHOR_ID = 1L;
    public static final Long NOT_REFERENCED_AUTHOR_ID = 6L;
    public static final Long NOT_EXISTS_AUTHOR_ID = 0L;
    private static final int PAGE_SIZE = 5;

    ParameterizedTypeReference<ResponsePage<AuthorDTO>> pageResponseType = new ParameterizedTypeReference<ResponsePage<AuthorDTO>>() {};
    ParameterizedTypeReference<List<AuthorDTO>> listResponseType = new ParameterizedTypeReference<List<AuthorDTO>>() {};
    ParameterizedTypeReference<AuthorDTO> authorResponseType = new ParameterizedTypeReference<AuthorDTO>() {};

    @Test
    public void findAllShouldReturnAllAuthor() {

        ResponseEntity<List<AuthorDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().size());
    }

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        AuthorSearchDTO searchDto = new AuthorSearchDTO();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(searchDto),
                pageResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_AUTHORS - PAGE_SIZE;

        AuthorSearchDTO searchDto = new AuthorSearchDTO();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(searchDto),
                pageResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void createShouldCreateANewAuthor() {

        AuthorDTO dto = new AuthorDTO();
        dto.setName("New Author");
        dto.setNationality("ES");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<AuthorDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(findAllResponse.getBody());
        assertEquals(TOTAL_AUTHORS + 1, findAllResponse.getBody().size());
        assertTrue(findAllResponse.getBody().stream().anyMatch(a -> "New Author".equals(a.getName()) && "ES".equals(a.getNationality())));
    }

    @Test
    public void createWithAnEmptyNameShouldReturnBadRequest() {

        AuthorDTO dto = new AuthorDTO();
        dto.setName("");
        dto.setNationality("ES");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateShouldReplaceAnExistingAuthor() {

        AuthorDTO dto = new AuthorDTO();
        dto.setName("Updated Author");
        dto.setNationality("FR");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<AuthorDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.GET,
                null,
                authorResponseType
        );

        assertNotNull(findByIdResponse);
        assertEquals(HttpStatus.OK, findByIdResponse.getStatusCode());
        assertNotNull(findByIdResponse.getBody());
        assertEquals("Updated Author", findByIdResponse.getBody().getName());
        assertEquals("FR", findByIdResponse.getBody().getNationality());
    }

    @Test
    public void updateWithNonExistentIdShouldReturnNotFound() {

        AuthorDTO dto = new AuthorDTO();
        dto.setName("Updated Author");
        dto.setNationality("FR");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_AUTHOR_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateWithAnEmptyNameShouldReturnBadRequest() {

        AuthorDTO dto = new AuthorDTO();
        dto.setName("");
        dto.setNationality("FR");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findByIdShouldReturnExpectedAuthor() {

        ResponseEntity<AuthorDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.GET,
                null,
                authorResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(NOT_REFERENCED_AUTHOR_ID, response.getBody().getId());
    }

    @Test
    public void findByNonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<AuthorDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_AUTHOR_ID,
                HttpMethod.GET,
                null,
                authorResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteAuthorShouldDeleteExpectedAuthor() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<AuthorDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_AUTHOR_ID,
                HttpMethod.GET,
                null,
                authorResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, findByIdResponse.getStatusCode());
    }

    @Test
    public void deleteNonExistentAuthorShouldReturnNotFoundError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_AUTHOR_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteReferencedAuthorShouldReturnConflictError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + GAME_REFERENCED_AUTHOR_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
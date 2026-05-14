package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.CategoryDTO;
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
public class CategoryIT extends AbstractIT {

    public static final String SERVICE_PATH = "/categories";

    // Basado en java/resources/data.sql
    private static final List<String> BOOTSTRAP_CATEGORY_NAMES = List.of("Eurogames", "Ameritrash", "Familiar");
    private static final Long GAME_REFERENCED_CATEGORY_ID = 1L;
    private static final Long NOT_REFERENCED_CATEGORY_ID = 2L;
    private static final Long NOT_EXISTS_CATEGORY_ID = 0L;
    private static final Long CATEGORY_TO_UPDATE_ID = 2L;

    private final ParameterizedTypeReference<List<CategoryDTO>> listResponseType = new ParameterizedTypeReference<>() {};
    private final ParameterizedTypeReference<CategoryDTO> categoryResponseType = new ParameterizedTypeReference<>() {};

    @Test
    public void findAllShouldReturnAllCategories() {

        ResponseEntity<List<CategoryDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), response.getBody().size());
        for (int i = 0; i < BOOTSTRAP_CATEGORY_NAMES.size(); i++) {
            assertEquals(BOOTSTRAP_CATEGORY_NAMES.get(i), response.getBody().get(i).getName());
        }
    }

    @Test
    public void findByIdShouldReturnSpecificCategory() {

        ResponseEntity<CategoryDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/1",
                HttpMethod.GET,
                null,
                categoryResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.getFirst(), response.getBody().getName());
    }

    @Test
    public void findByANonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<CategoryDTO> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CATEGORY_ID,
                HttpMethod.GET,
                null,
                categoryResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void createShouldCreateANewCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("New Category");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size() + 1, findAllResponse.getBody().size());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size() + 1, findAllResponse.getBody().getLast().getId());
        assertEquals("New Category", findAllResponse.getBody().getLast().getName());
    }

    @Test
    public void createWithAnEmptyNameShouldReturnAnError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), findAllResponse.getBody().size()); // No se ha creado la categoría
    }

    @Test
    public void createWithANullNameShouldReturnAnError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName(null);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.GET,
                null,
                listResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findAllResponse.getStatusCode());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), findAllResponse.getBody().size());
    }

    @Test
    public void updateShouldReplaceAnExistingCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(CATEGORY_TO_UPDATE_ID);
        dto.setName("Updated Category");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<CategoryDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID,
                HttpMethod.GET,
                null,
                categoryResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, findByIdResponse.getStatusCode());
        assertEquals("Updated Category", findByIdResponse.getBody().getName());
    }

    @Test
    public void updateWithNonExistentIdShouldReturnANotFoundError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId((long) 4);
        dto.setName("Updated Category");

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CATEGORY_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteACategory() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_REFERENCED_CATEGORY_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<CategoryDTO> findByIdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID,
                HttpMethod.GET,
                null,
                categoryResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, findByIdResponse.getStatusCode());
    }

    @Test
    public void deleteWithNonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CATEGORY_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteReferencedCategoryShouldReturnConflictError() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + GAME_REFERENCED_CATEGORY_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}

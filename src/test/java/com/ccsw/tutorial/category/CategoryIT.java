package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
public class CategoryIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/categories";

    // Based on java/resources/data.sql
    private static final List<String> BOOTSTRAP_CATEGORY_NAMES = List.of("Eurogames", "Ameritrash", "Familiar");

    private static final int NON_EXISTENT_ID = BOOTSTRAP_CATEGORY_NAMES.size() + 1;
    private static final int CATEGORY_TO_UPDATE_ID = 2;
    

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ParameterizedTypeReference<List<CategoryDTO>> listResponseType = new ParameterizedTypeReference<>() {};
    private final ParameterizedTypeReference<CategoryDTO> categoryResponseType = new ParameterizedTypeReference<>() {};

    @Test
    public void findAllShouldReturnAllCategories() {

        ResponseEntity<List<CategoryDTO>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

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

        ResponseEntity<CategoryDTO> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/1", HttpMethod.GET, null, categoryResponseType);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.getFirst(), response.getBody().getName());
    }

    @Test
    public void findByANonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<CategoryDTO> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NON_EXISTENT_ID, HttpMethod.GET, null, categoryResponseType);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void createShouldCreateANewCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("New Category");

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size() + 1, findAllResponse.getBody().size());
        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size() + 1, findAllResponse.getBody().getLast().getId());
        assertEquals("New Category", findAllResponse.getBody().getLast().getName());
    }

    @Test
    public void createWithAnEmptyNameShouldReturnAnError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("");

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), findAllResponse.getBody().size()); // No se ha creado la categoría
    }

    @Test
    public void createWithANullNameShouldReturnAnError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName(null);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), findAllResponse.getBody().size()); // No se ha creado la categoría
    }

    @Test
    public void updateShouldReplaceAnExistingCategory() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId((long) CATEGORY_TO_UPDATE_ID);
        dto.setName("Updated Category");

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size(), findAllResponse.getBody().size());
        assertEquals(CATEGORY_TO_UPDATE_ID, findAllResponse.getBody().get(CATEGORY_TO_UPDATE_ID - 1).getId());
        assertEquals("Updated Category", findAllResponse.getBody().get(CATEGORY_TO_UPDATE_ID - 1).getName());
    }

    @Test
    public void updateWithNonExistentIdShouldReturnANotFoundError() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId((long) 4);
        dto.setName("Updated Category");

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NON_EXISTENT_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteACategory() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID, HttpMethod.DELETE, null, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<List<CategoryDTO>> findAllResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, listResponseType);

        assertEquals(BOOTSTRAP_CATEGORY_NAMES.size() - 1, findAllResponse.getBody().size());

        ResponseEntity<CategoryDTO> findByIdResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + CATEGORY_TO_UPDATE_ID, HttpMethod.GET, null, categoryResponseType);

        assertEquals(HttpStatus.NOT_FOUND, findByIdResponse.getStatusCode());
    }

    @Test
    public void deleteWithNonExistentIdShouldReturnNotFoundError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NON_EXISTENT_ID, HttpMethod.DELETE, null, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}

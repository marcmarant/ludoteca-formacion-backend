package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.category.model.CategoryDTO;
import com.ccsw.tutorial.common.AbstractIT;
import com.ccsw.tutorial.game.model.GameDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameIT extends AbstractIT {

    public static final String SERVICE_PATH = "/games";

    // Based on java/resources/data.sql
    private static final int TOTAL_GAMES = 6;
    private static final Long EXISTS_GAME_ID = 1L;
    private static final Long NOT_EXISTS_GAME_ID = 0L;
    private static final Long EXISTS_AUTHOR_ID = 1L;
    private static final Long EXISTS_CATEGORY = 3L;
    private static final Long NOT_EXISTS_AUTHOR_ID = 99L;
    private static final Long NOT_EXISTS_CATEGORY = 99L;

    private static final String NOT_EXISTS_TITLE = "NotExists";
    private static final String EXISTS_TITLE = "Aventureros";
    private static final String NEW_TITLE = "Nuevo juego";
    private static final String EXISTS_AUTHOR = "Alan";

    private static final String TITLE_PARAM = "title";
    private static final String AUTHOR_PARAM = "author";
    private static final String CATEGORY_ID_PARAM = "idCategory";

    ParameterizedTypeReference<List<GameDTO>> listResponseType = new ParameterizedTypeReference<>(){};

    private String getUrlWithParams(){
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH)
                .queryParam(TITLE_PARAM, "{" + TITLE_PARAM +"}")
                .queryParam(AUTHOR_PARAM, "{" + AUTHOR_PARAM +"}")
                .queryParam(CATEGORY_ID_PARAM, "{" + CATEGORY_ID_PARAM +"}")
                .encode()
                .toUriString();
    }

    private GameDTO createGameDto(String title, String age, Long authorId, Long categoryId) {
        GameDTO dto = new GameDTO();
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setId(authorId);

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(categoryId);

        dto.setTitle(title);
        dto.setAge(age);
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);
        return dto;
    }

    @Test
    public void findWithoutFiltersShouldReturnAllGames() {

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_GAMES, response.getBody().size());
    }

    @Test
    public void findExistsTitleShouldReturnGames() {
        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsAuthorShouldReturnGames() {
        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(AUTHOR_PARAM, EXISTS_AUTHOR);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsCategoryShouldReturnGames() {
        int GAMES_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsTitleAndCategoryShouldReturnGames() {
        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsTitleShouldReturnEmpty() {
        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsCategoryShouldReturnEmpty() {
        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsTitleOrCategoryShouldReturnEmpty() {
        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void createShouldCreateNewGame() {

        GameDTO dto = createGameDto(NEW_TITLE, "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> createResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(createResponse);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void createWithAnEmptyTitleShouldReturnBadRequest() {

        GameDTO dto = createGameDto("", "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

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
    public void createWithAnEmptyAgeShouldReturnBadRequest() {

        GameDTO dto = createGameDto("Test Game", "", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

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
    public void createWithNonExistentAuthorShouldReturnNotFound() {

        GameDTO dto = createGameDto("Test Game", "18", NOT_EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createWithNonExistentCategoryShouldReturnNotFound() {

        GameDTO dto = createGameDto("Test Game", "18", EXISTS_AUTHOR_ID, NOT_EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateExistentGameShouldModifyGame() {

        GameDTO dto = createGameDto(NEW_TITLE, "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> updateResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(AUTHOR_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(
                getUrlWithParams(),
                HttpMethod.GET,
                null,
                listResponseType,
                params
        );

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(EXISTS_GAME_ID, response.getBody().get(0).getId());
    }

    @Test
    public void updateNotExistentGameShouldReturnNotFoundError() {

        GameDTO dto = createGameDto(NEW_TITLE, "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_GAME_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateWithAnEmptyTitleShouldReturnBadRequest() {

        GameDTO dto = createGameDto("", "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateWithNonExistentAuthorShouldReturnNotFound() {

        GameDTO dto = createGameDto("Updated Title", "18", NOT_EXISTS_AUTHOR_ID, EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateWithNonExistentCategoryShouldReturnNotFound() {

        GameDTO dto = createGameDto("Updated Title", "18", EXISTS_AUTHOR_ID, NOT_EXISTS_CATEGORY);

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}

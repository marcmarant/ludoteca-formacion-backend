package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.category.model.CategoryDTO;
import com.ccsw.tutorial.game.model.GameDTO;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/games";

    // Based on java/resources/data.sql
    private static final int BOOTSTRAP_EXISTENT_GAMES_NUMBER = 6;

    public static final Long EXISTS_GAME_ID = 1L;
    public static final Long NOT_EXISTS_GAME_ID = 0L;
    private static final String NOT_EXISTS_TITLE = "NotExists";
    private static final String EXISTS_TITLE = "Aventureros";
    private static final String NEW_TITLE = "Nuevo juego";
    private static final Long NOT_EXISTS_CATEGORY = 0L;
    private static final Long EXISTS_CATEGORY = 3L;

    private static final String TITLE_PARAM = "title";
    private static final String CATEGORY_ID_PARAM = "idCategory";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<GameDTO>> responseType = new ParameterizedTypeReference<List<GameDTO>>(){};

    private String getUrlWithParams(){
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH)
                .queryParam(TITLE_PARAM, "{" + TITLE_PARAM +"}")
                .queryParam(CATEGORY_ID_PARAM, "{" + CATEGORY_ID_PARAM +"}")
                .encode()
                .toUriString();
    }

    @Test
    public void findWithoutFiltersShouldReturnAllGamesInDB() {

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(BOOTSTRAP_EXISTENT_GAMES_NUMBER, response.getBody().size());
    }

    @Test
    public void findExistsTitleShouldReturnGames() {

        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsCategoryShouldReturnGames() {

        int GAMES_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsTitleAndCategoryShouldReturnGames() {

        int GAMES_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsTitleShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsCategoryShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, null);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsTitleOrCategoryShouldReturnEmpty() {

        int GAMES_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());

        params.put(TITLE_PARAM, NOT_EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, EXISTS_CATEGORY);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());

        params.put(TITLE_PARAM, EXISTS_TITLE);
        params.put(CATEGORY_ID_PARAM, NOT_EXISTS_CATEGORY);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(GAMES_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void createShouldCreateNewGame() {

        GameDTO dto = new GameDTO();
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setId(1L);

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(1L);

        dto.setTitle(NEW_TITLE);
        dto.setAge("18");
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(dto), Void.class);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void UpdateExistentGameShouldModifyGame() {

        GameDTO dto = new GameDTO();
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setId(1L);

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(1L);

        dto.setTitle(NEW_TITLE);
        dto.setAge("18");
        dto.setAuthor(authorDto);
        dto.setCategory(categoryDto);

        Map<String, Object> params = new HashMap<>();
        params.put(TITLE_PARAM, NEW_TITLE);
        params.put(CATEGORY_ID_PARAM, null);

        ResponseEntity<List<GameDTO>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(EXISTS_GAME_ID, response.getBody().get(0).getId());
    }

    @Test
    public void UpdateNotExistentGameShouldReturnNotFoundError() {

        GameDTO dto = new GameDTO();
        dto.setTitle(NEW_TITLE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}

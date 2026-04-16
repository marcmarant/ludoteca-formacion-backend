package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.AbstractIT;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
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
public class AuthorIT extends AbstractIT {

    public static final String SERVICE_PATH = "/authors";

    // Based on java/resources/data.sql
    private static final int TOTAL_AUTHORS = 6;
    public static final Long EXISTS_AUTHOR_ID = 1L;
    public static final Long NOT_EXISTS_AUTHOR_ID = 0L;
    private static final int PAGE_SIZE = 5;

    ParameterizedTypeReference<ResponsePage<AuthorDTO>> pageResponseType = new ParameterizedTypeReference<ResponsePage<AuthorDTO>>(){};
    ParameterizedTypeReference<List<AuthorDTO>> listResponseType = new ParameterizedTypeReference<List<AuthorDTO>>(){};
    ParameterizedTypeReference<AuthorDTO> entityResponseType = new ParameterizedTypeReference<AuthorDTO>(){};

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

    // TODO COMPLETE TESTS

    /*@Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        AuthorSearchDTO searchDto = new AuthorSearchDTO();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDTO>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, new HttpEntity<>(searchDto), pageResponseType);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }*/

    /*@Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_AUTHORS - PAGE_SIZE;

        AuthorSearchDto searchDto = new AuthorSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), pageResponseType);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }*/

    @Test
    public void deleteNonExistentAuthorShouldReturnNotFoundException() {

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
    public void deleteReferencedAuthorShouldReturnDeleteEntityConflictException() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_AUTHOR_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
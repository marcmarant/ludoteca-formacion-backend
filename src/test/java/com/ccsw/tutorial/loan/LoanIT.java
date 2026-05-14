package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDTO;
import com.ccsw.tutorial.common.AbstractIT;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDTO;
import com.ccsw.tutorial.loan.model.LoanDTO;
import com.ccsw.tutorial.loan.model.LoanSearchDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT extends AbstractIT {

    public static final String SERVICE_PATH = "/loans";

    // Basado en java/resources/data.sql
    private static final int TOTAL_LOANS = 5;
    private static final Long EXISTS_LOAN_ID = 1L;
    private static final Long DELETE_LOAN_ID = 2L;
    private static final Long NOT_EXISTS_LOAN_ID = 0L;
    private static final Long GAME_ON_LOAN_ID = 1L;
    private static final Long GAME_WITHOUT_LOANS_ID = 6L;
    private static final Long CLIENT_WITH_LOANS_ID = 1L;
    private static final Long CLIENT_WITHOUT_LOANS_ID = 3L;

    private final ParameterizedTypeReference<ResponsePage<LoanDTO>> pageResponseType = new ParameterizedTypeReference<>() {};

    @Test
    public void findPageShouldReturnExpectedPage() {

        LoanSearchDTO searchDto = new LoanSearchDTO();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));
        searchDto.setTitle("Mars");
        searchDto.setClientId(CLIENT_WITH_LOANS_ID);
        searchDto.setDate(LocalDate.of(2026, 4, 10));

        ResponseEntity<ResponsePage<LoanDTO>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(searchDto),
                pageResponseType
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void createShouldCreateALoan() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 5)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<ResponsePage<LoanDTO>> searchResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(buildSearchDto("Azul", CLIENT_WITHOUT_LOANS_ID, LocalDate.of(2026, 4, 1))),
                pageResponseType
        );

        assertNotNull(searchResponse.getBody());
        assertEquals(1, searchResponse.getBody().getTotalElements());
    }

    @Test
    public void createALoanWithAGameAlreadyOnLoanShouldReturnBadRequest() {
        LoanDTO dto = createLoanDto(
                GAME_ON_LOAN_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 16)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createALoanWithAnInvalidTimeIntervalShouldReturnBadRequest() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 20)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createALoanWithLoanDateAfterReturnDateShouldReturnBadRequest() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 10)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.POST,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateShouldReplaceExpectedLoan() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 5)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_LOAN_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<ResponsePage<LoanDTO>> searchResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(buildSearchDto("Azul", CLIENT_WITHOUT_LOANS_ID, LocalDate.of(2026, 4, 1))),
                pageResponseType
        );

        assertNotNull(searchResponse.getBody());
        assertEquals(1, searchResponse.getBody().getTotalElements());
    }

    @Test
    public void updateNoExistentLoanShouldReturnNotFound() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 5)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_LOAN_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateALoanWithAGameAlreadyOnLoanShouldReturnBadRequest() {
        LoanDTO dto = createLoanDto(
                GAME_ON_LOAN_ID,
                CLIENT_WITH_LOANS_ID,
                LocalDate.of(2026, 4, 25),
                LocalDate.of(2026, 4, 30)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_LOAN_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateALoanWithAnInvalidTimeIntervalShouldReturnBadRequest() {

        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 20)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_LOAN_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateALoanWithLoanDateAfterReturnDateShouldReturnBadRequest() {
        LoanDTO dto = createLoanDto(
                GAME_WITHOUT_LOANS_ID,
                CLIENT_WITHOUT_LOANS_ID,
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 10)
        );

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_LOAN_ID,
                HttpMethod.PUT,
                buildAuthEntity(dto),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteExpectedLoan() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                Void.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<ResponsePage<LoanDTO>> searchResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/search",
                HttpMethod.POST,
                new HttpEntity<>(buildSearchDto("Aventureros al tren", CLIENT_WITH_LOANS_ID, LocalDate.of(2026, 4, 12))),
                pageResponseType
        );

        assertNotNull(searchResponse.getBody());
        assertEquals(0, searchResponse.getBody().getTotalElements());
    }

    @Test
    public void deleteNonExistentLoanShouldReturnNotFound() {

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_LOAN_ID,
                HttpMethod.DELETE,
                buildAuthEntity(),
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private LoanDTO createLoanDto(Long gameId, Long clientId, LocalDate loanDate, LocalDate returnDate) {
        LoanDTO dto = new LoanDTO();
        dto.setLoanDate(loanDate);
        dto.setReturnDate(returnDate);

        GameDTO gameDto = new GameDTO();
        gameDto.setId(gameId);
        dto.setGame(gameDto);

        ClientDTO clientDto = new ClientDTO();
        clientDto.setId(clientId);
        dto.setClient(clientDto);

        return dto;
    }

    private LoanSearchDTO buildSearchDto(String title, Long clientId, LocalDate date) {
        LoanSearchDTO searchDto = new LoanSearchDTO();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));
        searchDto.setTitle(title);
        searchDto.setClientId(clientId);
        searchDto.setDate(date);
        return searchDto;
    }
}

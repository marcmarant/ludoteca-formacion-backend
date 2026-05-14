package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientRepository;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.common.exception.GameNotAvailableToLoanException;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDTO;
import com.ccsw.tutorial.loan.model.LoanSearchDTO;
import com.ccsw.tutorial.game.model.GameDTO;
import com.ccsw.tutorial.client.model.ClientDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private static final Long EXISTS_LOAN_ID = 1L;
    private static final Long NOT_EXISTS_LOAN_ID = 0L;
    private static final Long EXISTS_GAME_ID = 1L;
    private static final Long EXISTS_CLIENT_ID = 1L;
    private static final LocalDate LOAN_DATE = LocalDate.of(2026, 4, 10);
    private static final LocalDate RETURN_DATE = LocalDate.of(2026, 4, 20);

    private LoanDTO createLoanDto(Long id, Long gameId, Long clientId) {
        LoanDTO dto = new LoanDTO();

        dto.setId(id);
        dto.setLoanDate(LOAN_DATE);
        dto.setReturnDate(RETURN_DATE);

        com.ccsw.tutorial.game.model.GameDTO gameDTO = new GameDTO();
        gameDTO.setId(gameId);
        dto.setGame(gameDTO);

        com.ccsw.tutorial.client.model.ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        dto.setClient(clientDTO);

        return dto;
    }

    @Test
    public void findPageShouldReturnExpectedPage() {

        LoanSearchDTO dto = new LoanSearchDTO();
        dto.setPageable(new PageableRequest(0, 5));
        dto.setTitle("Mars");
        dto.setClientId(EXISTS_CLIENT_ID);
        dto.setDate(LocalDate.of(2026, 4, 10));

        Page<Loan> expectedPage = new PageImpl<>(List.of(new Loan()));

        when(loanRepository.findAll(org.mockito.ArgumentMatchers.any(), any(Pageable.class))).thenReturn(expectedPage);

        Page<Loan> page = loanService.findPage(dto);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        verify(loanRepository).findAll(org.mockito.ArgumentMatchers.any(), any(Pageable.class));
    }

    @Test
    public void createShouldCreateALoan() {

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);

        Game game = new Game();
        game.setId(EXISTS_GAME_ID);

        Client client = new Client();
        client.setId(EXISTS_CLIENT_ID);

        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(game));
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));
        when(loanRepository.existsByGameIdAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(EXISTS_GAME_ID, LOAN_DATE, RETURN_DATE))
                .thenReturn(false);

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);

        loanService.create(dto);

        verify(loanRepository).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        assertEquals(dto.getLoanDate(), savedLoan.getLoanDate());
        assertEquals(dto.getReturnDate(), savedLoan.getReturnDate());
        assertEquals(EXISTS_GAME_ID, savedLoan.getGame().getId());
        assertEquals(EXISTS_CLIENT_ID, savedLoan.getClient().getId());
    }

    @Test
    public void createALoanWithAGameAlreadyOnLoanShouldThrowGameNotAvailableToLoanException() {

        Game game = new Game();
        game.setId(EXISTS_GAME_ID);

        Client client = new Client();
        client.setId(EXISTS_CLIENT_ID);

        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(game));
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));
        when(loanRepository.existsByGameIdAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(EXISTS_GAME_ID, LOAN_DATE, RETURN_DATE))
                .thenReturn(true);

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);

        assertThrows(GameNotAvailableToLoanException.class, () -> loanService.create(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void createALoanWithAnInvalidTimeIntervalShouldThrowIllegalArgumentException() {

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);
        dto.setLoanDate(LOAN_DATE);
        dto.setReturnDate(RETURN_DATE.plusDays(10));

        assertThrows(IllegalArgumentException.class, () -> loanService.create(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void createALoanWithLoanDateAfterReturnDateShouldThrowIllegalArgumentException() {

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);
        dto.setLoanDate(RETURN_DATE);
        dto.setReturnDate(LOAN_DATE);

        assertThrows(IllegalArgumentException.class, () -> loanService.create(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void updateShouldReplaceExpectedLoan() {

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);

        Game existingGame = new Game();
        existingGame.setId(EXISTS_GAME_ID);

        Client existingClient = new Client();
        existingClient.setId(EXISTS_CLIENT_ID);

        Game newGame = new Game();
        newGame.setId(99L);

        Client newClient = new Client();
        newClient.setId(99L);

        Loan existingLoan = new Loan();
        existingLoan.setId(EXISTS_LOAN_ID);
        existingLoan.setLoanDate(LocalDate.of(2026, 3, 9));
        existingLoan.setReturnDate(LocalDate.of(2026, 5, 21));
        existingLoan.setClient(existingClient);
        existingLoan.setGame(existingGame);

        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(existingLoan));
        when(gameRepository.findById(99L)).thenReturn(Optional.of(newGame));
        when(clientRepository.findById(99L)).thenReturn(Optional.of(newClient));
        when(loanRepository.existsByGameIdAndIdNotAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(99L, EXISTS_LOAN_ID, LOAN_DATE, RETURN_DATE))
                .thenReturn(false);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanDTO dto = createLoanDto(EXISTS_LOAN_ID, 99L, 99L);

        loanService.update(dto);

        verify(loanRepository).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();

        assertEquals(dto.getId(), savedLoan.getId());
        assertEquals(dto.getLoanDate(), savedLoan.getLoanDate());
        assertEquals(dto.getReturnDate(), savedLoan.getReturnDate());
    }

    @Test
    public void updateNonExistentLoanShouldThrowEntityNotFoundException() {

        LoanDTO dto = createLoanDto(NOT_EXISTS_LOAN_ID, EXISTS_GAME_ID, EXISTS_CLIENT_ID);

        when(loanRepository.findById(NOT_EXISTS_LOAN_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.update(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void updateALoanWithAGameAlreadyOnLoanShouldThrowGameNotAvailableToLoanException() {

        Game existingGame = new Game();
        existingGame.setId(EXISTS_GAME_ID);

        Client existingClient = new Client();
        existingClient.setId(EXISTS_CLIENT_ID);

        Game newGame = new Game();
        newGame.setId(99L);

        Client newClient = new Client();
        newClient.setId(99L);

        Loan existingLoan = new Loan();
        existingLoan.setId(EXISTS_LOAN_ID);
        existingLoan.setLoanDate(LocalDate.of(2026, 3, 9));
        existingLoan.setReturnDate(LocalDate.of(2026, 5, 21));
        existingLoan.setClient(existingClient);
        existingLoan.setGame(existingGame);

        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(existingLoan));
        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(newGame));
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(newClient));
        when(loanRepository.existsByGameIdAndIdNotAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(99L, EXISTS_LOAN_ID, LOAN_DATE, RETURN_DATE))
                .thenReturn(true);

        LoanDTO dto = createLoanDto(EXISTS_LOAN_ID, EXISTS_GAME_ID, EXISTS_CLIENT_ID);

        assertThrows(GameNotAvailableToLoanException.class, () -> loanService.update(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void updateALoanWithLoanWithAnInvalidTimeIntervalShouldThrowIllegalArgumentException() {

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);
        dto.setLoanDate(LOAN_DATE);
        dto.setReturnDate(RETURN_DATE.plusDays(10));

        assertThrows(IllegalArgumentException.class, () -> loanService.update(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void updateALoanWithLoanDateAfterReturnDateShouldThrowIllegalArgumentException() {

        LoanDTO dto = createLoanDto(null, EXISTS_GAME_ID, EXISTS_CLIENT_ID);
        dto.setLoanDate(RETURN_DATE);
        dto.setReturnDate(LOAN_DATE);

        assertThrows(IllegalArgumentException.class, () -> loanService.update(dto));

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void deleteShouldDeleteExpectedLoan() {

        Loan mockLoan = mock(Loan.class);

        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(mockLoan));

        loanService.delete(EXISTS_LOAN_ID);

        verify(loanRepository).deleteById(EXISTS_LOAN_ID);
    }

    @Test
    public void deleteNonExistentLoanShouldThrowEntityNotFoundException() {

        when(loanRepository.findById(NOT_EXISTS_LOAN_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanService.delete(NOT_EXISTS_LOAN_ID));

        verify(loanRepository, never()).deleteById(anyLong());
    }

}



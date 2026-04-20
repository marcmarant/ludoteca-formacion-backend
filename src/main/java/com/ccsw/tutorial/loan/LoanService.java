package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDTO;
import com.ccsw.tutorial.loan.model.LoanSearchDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import com.ccsw.tutorial.common.exception.GameNotAvailableToLoanException;

public interface LoanService {

    /**
     * Recupera una pagina de prestamos filtrada opcionalmente por titulo, cliente y fecha.
     *
     * @param dto filtros y paginacion
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findPage(LoanSearchDTO dto);

    /**
     * Crea un nuevo prestamo
     *
     * @param dto datos del prestamo
     * @throws EntityNotFoundException si no existe el juego o el cliente
     * @throws GameNotAvailableToLoanException si el juego no esta disponible para ser prestado
     */
    void create(LoanDTO dto) throws EntityNotFoundException;

    /**
     * Actualiza un prestamo existente
     *
     * @param dto datos del prestamo incluyendo id
     * @throws EntityNotFoundException si no existe el prestamo, juego o cliente
     * @throws GameNotAvailableToLoanException si el juego no esta disponible para ser prestado
     */
    void update(LoanDTO dto) throws EntityNotFoundException;

    /**
     * Elimina un prestamo existente
     *
     * @param id identificador del prestamo
     * @throws EntityNotFoundException si no existe el prestamo
     */
    void delete(Long id) throws EntityNotFoundException;
}


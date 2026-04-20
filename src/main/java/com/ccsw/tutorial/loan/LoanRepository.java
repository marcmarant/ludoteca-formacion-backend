package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.loan.model.Loan;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    /**
     * Recupera un listado filtrado y paginado de {@link Loan}
     *
     * @param spec especificacion con los filtros de busqueda
     * @param pageable pageable
     * @return {@link Page} de {@link Author}
     */
	@Override
	@EntityGraph(attributePaths = {"game", "client"})
	Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

	/**
	 * Deevuelve si existe un prestamo del juego indicado en el intervalo de fechas indicado
	 *
	 * @param gameId id del juego
	 * @param loanDate fecha de prestamo
	 * @param returnDate fecha de devolucion
	 * @return true si existe algun solapamiento, false en caso contrario
	 */
	boolean existsByGameIdAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(Long gameId, LocalDate loanDate, LocalDate returnDate);

	/**
	 * Deevuelve si existe otro prestamo del juego indicado en el intervalo de fechas indicado,
	 * excluyendo el prestamo actual por id
	 *
	 * @param gameId id del juego
	 * @param id id del prestamo a excluir
	 * @param loanDate fecha de prestamo
	 * @param returnDate fecha de devolucion
	 * @return true si existe algun solapamiento, false en caso contrario
	 */
	boolean existsByGameIdAndIdNotAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(Long gameId, Long id, LocalDate loanDate, LocalDate returnDate);
}

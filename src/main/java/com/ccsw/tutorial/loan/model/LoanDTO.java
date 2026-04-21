package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.ClientDTO;
import com.ccsw.tutorial.game.model.GameDTO;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LoanDTO {

	private Long id;

	@NotNull(message = "La fecha de prestamo no puede estar vacia")
	private LocalDate loanDate;

    @NotNull(message = "La fecha de devolucion no puede estar vacia")
	private LocalDate returnDate;

    @NotNull(message = "El juego no puede estar vacia")
	private GameDTO game;

    @NotNull(message = "El cliente no puede estar vacia")
	private ClientDTO client;

	/**
	 * @return id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id new value of {@link #getId}.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return loanDate
	 */
	public LocalDate getLoanDate() {
		return this.loanDate;
	}

	/**
	 * @param loanDate new value of {@link #getLoanDate}.
	 */
	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	/**
	 * @return returnDate
	 */
	public LocalDate getReturnDate() {
		return this.returnDate;
	}

	/**
	 * @param returnDate new value of {@link #getReturnDate}.
	 */
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	/**
	 * @return game
	 */
	public GameDTO getGame() {
		return this.game;
	}

	/**
	 * @param game new value of {@link #getGame}.
	 */
	public void setGame(GameDTO game) {
		this.game = game;
	}

	/**
	 * @return client
	 */
	public ClientDTO getClient() {
		return this.client;
	}

	/**
	 * @param client new value of {@link #getClient}.
	 */
	public void setClient(ClientDTO client) {
		this.client = client;
	}
}

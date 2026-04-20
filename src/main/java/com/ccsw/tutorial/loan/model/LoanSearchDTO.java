package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO con la información de paginacion y los posibles filtros de busqueda por
 * titulo del juego, cliente y/o fecha de prestamo.
 */
public class LoanSearchDTO {

	@NotNull(message = "pageable is required")
	private PageableRequest pageable;

	private String title;
	private Long clientId;
	private LocalDate date;

	public PageableRequest getPageable() {
        return pageable;
	}

	public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}


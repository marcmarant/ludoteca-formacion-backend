package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "loan_date", nullable = false)
    @NotBlank
    private LocalDate loanDate;

    @Column(name = "return_date", nullable = false)
    @NotBlank
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

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
    public Game getGame() {
        return this.game;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @return client
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(Client client) {
        this.client = client;
    }

}

package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientRepository;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDTO;
import com.ccsw.tutorial.loan.model.LoanSearchDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Page<Loan> findPage(LoanSearchDTO dto) {

        LoanSpecification titleSpec = new LoanSpecification(new SearchCriteria("game.title", ":", dto.getTitle()));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", dto.getClientId()));
        LoanSpecification dateSpec = new LoanSpecification(new SearchCriteria("loanDate", ":", dto.getDate()));
        Specification<Loan> specification = titleSpec.and(clientSpec).and(dateSpec);

        return this.loanRepository.findAll(specification, dto.getPageable().getPageable());
    }

    @Override
    public void create(LoanDTO dto) throws EntityNotFoundException {
        Loan loan = new Loan();

        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(this.gameRepository.findById(dto.getGame().getId()).orElseThrow(
                () -> new EntityNotFoundException("Juego " + loan.getGame().getId() + " no encntrado")
        ));
        loan.setClient(this.clientRepository.findById(dto.getClient().getId()).orElseThrow(
                () -> new EntityNotFoundException("Cliente " + loan.getClient().getId() + " no encontrado")
        ));
        if (this.loanRepository.existsByGameIdAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(
                loan.getGame().getId(),
                loan.getReturnDate(),
                loan.getLoanDate())
        ) {
            throw new IllegalArgumentException("El juego no está disponible en el intervalo de fechas indicado");
        }

        this.loanRepository.save(loan);
    }

    @Override
    public void update(LoanDTO dto) throws EntityNotFoundException {

        Loan loan = this.loanRepository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Préstamo " + dto.getId() + " no encontrado")
        );

        BeanUtils.copyProperties(dto, loan,"game", "client");

        loan.setGame(this.gameRepository.findById(dto.getGame().getId()).orElseThrow(
                () -> new EntityNotFoundException("Juego " + loan.getGame().getId() + " no encontrado")
        ));
        loan.setClient(this.clientRepository.findById(dto.getClient().getId()).orElseThrow(
                () -> new EntityNotFoundException("Cliente " + loan.getClient().getId() + " no encontrado")
        ));
        if (this.loanRepository.existsByGameIdAndIdNotAndLoanDateLessThanEqualAndReturnDateGreaterThanEqual(
                loan.getGame().getId(),
                loan.getId(),
                loan.getReturnDate(),
                loan.getLoanDate())
        ) {
            throw new IllegalArgumentException("El juego no está disponible en el intervalo de fechas indicado");
        }

        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (this.loanRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Préstamo " + id + " no encontrado");
        }
        this.loanRepository.deleteById(id);
    }
}


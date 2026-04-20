package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.LoanDTO;
import com.ccsw.tutorial.loan.model.LoanSearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Loans", description = "API of Loan")
@RequestMapping(value = "/loans")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ModelMapper mapper;

    @Operation(summary = "Find a page", description = "Method that returns a page of loans filtered by title, client and date")
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public Object findPage(@Valid @RequestBody LoanSearchDTO dto) {
        return this.loanService.findPage(dto).map(loan -> this.mapper.map(loan, LoanDTO.class));
    }

    @Operation(summary = "Create", description = "Method that creates a new loan")
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody LoanDTO dto) {
        this.loanService.create(dto);
    }

    @Operation(summary = "Update", description = "Method that updates an existing loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(name = "id") Long id, @Valid @RequestBody LoanDTO dto) {
        dto.setId(id);
        this.loanService.update(dto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        this.loanService.delete(id);
    }
}


package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import com.ccsw.tutorial.loan.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(ClientDTO dto) {
        String name = dto.getName();

        if (this.clientRepository.existsByName(name)) {
            throw new IllegalArgumentException("Client name: " + name + " already exists");
        }
        Client client = new Client();
        client.setName(name);

        this.clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ClientDTO dto) {
        Optional<Client> optClient = this.clientRepository.findById(dto.getId());

        if (optClient.isEmpty()) {
            throw new EntityNotFoundException();
        }
        if (this.clientRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
            throw new IllegalArgumentException("El nombre de cliente: " + dto.getName() + " ya existe");
        }
        Client client = optClient.get();
        client.setName(dto.getName());

        this.clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (this.clientRepository.findById(id).orElse(null) == null) {
            throw new EntityNotFoundException("Cliente " + id.toString() + " no encontrado");
        }
        if (this.loanRepository.existsByClient_Id(id)) {
            throw new DeleteEntityConflictException("No se puede borrar el cliente " + id + " porque esta asociado a un préstamo");
        }
        this.clientRepository.deleteById(id);
    }
}
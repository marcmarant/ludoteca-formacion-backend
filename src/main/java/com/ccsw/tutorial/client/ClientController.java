package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.ClientDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Clients", description = "API of Client")
@RequestMapping(value = "/clients")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {

	@Autowired
	ClientService clientService;

	@Autowired
	ModelMapper mapper;

	@Operation(summary = "Find All", description = "Method that return a list of all clients")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<ClientDTO> findAll() {
		return this.clientService.findAll().stream().map(e -> this.mapper.map(e, ClientDTO.class)).collect(Collectors.toList());
	}

	@Operation(summary = "Create", description = "Method that creates a new client")
	@RequestMapping(path = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody ClientDTO dto) {
		this.clientService.create(dto);
	}

	@Operation(summary = "Update", description = "Method that updates an existing client")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable(name = "id") Long id, @Valid @RequestBody ClientDTO dto) {
		dto.setId(id);
		this.clientService.update(dto);
	}

	@Operation(summary = "Delete", description = "Method that deletes a client")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(name = "id") Long id) {
		this.clientService.delete(id);
	}
}

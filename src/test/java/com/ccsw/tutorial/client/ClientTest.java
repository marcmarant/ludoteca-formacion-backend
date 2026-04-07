package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private ClientServiceImpl clientService;

	public static final Long EXISTS_CLIENT_ID = 1L;
	public static final Long NOT_EXISTS_CLIENT_ID = 7L;

	@Test
	public void findAllShouldReturnAllClients() {

		List<Client> list = new ArrayList<>();
		list.add(mock(Client.class));

		when(clientRepository.findAll()).thenReturn(list);

		List<Client> clients = clientService.findAll();

		assertNotNull(clients);
		assertEquals(1, clients.size());
	}

	@Test
	public void createShouldCreateAValidClient() {

		ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

		when(clientRepository.existsByName("Test Client")).thenReturn(false);
		when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ClientDTO dto = new ClientDTO();
		dto.setName("Test Client");

		clientService.create(dto);

		verify(clientRepository).save(clientCaptor.capture());

		Client savedClient = clientCaptor.getValue();

		assertEquals(dto.getName(), savedClient.getName());
	}

	@Test
	public void createShouldDenyDuplicatedClientName() {

		ClientDTO dto = new ClientDTO();
		dto.setName("Existing Client");

		when(clientRepository.existsByName(dto.getName())).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> clientService.create(dto));

		verify(clientRepository, never()).save(any(Client.class));
	}

	@Test
	public void updateShouldReplaceAClient() {

		ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

		Client existingClient = new Client();
		existingClient.setId(EXISTS_CLIENT_ID);
		existingClient.setName("Old Client");

		when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(existingClient));
		when(clientRepository.existsByNameAndIdNot("Updated Client", EXISTS_CLIENT_ID)).thenReturn(false);
		when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ClientDTO dto = new ClientDTO();
		dto.setId(EXISTS_CLIENT_ID);
		dto.setName("Updated Client");

		clientService.update(dto);

		verify(clientRepository).save(clientCaptor.capture());

		Client savedClient = clientCaptor.getValue();

		assertEquals(dto.getId(), savedClient.getId());
		assertEquals(dto.getName(), savedClient.getName());
	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenClientDoesNotExist() {

		ClientDTO dto = new ClientDTO();
		dto.setId(NOT_EXISTS_CLIENT_ID);
		dto.setName("Updated Client");

		when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> clientService.update(dto));

		verify(clientRepository, never()).save(any(Client.class));
	}

	@Test
	public void updateShouldDenyDuplicatedClientName() {

		Client existingClient = new Client();
		existingClient.setId(EXISTS_CLIENT_ID);
		existingClient.setName("Old Client");

		ClientDTO dto = new ClientDTO();
		dto.setId(EXISTS_CLIENT_ID);
		dto.setName("Duplicated Client");

		when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(existingClient));
		when(clientRepository.existsByNameAndIdNot(dto.getName(), dto.getId())).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> clientService.update(dto));

		verify(clientRepository, never()).save(any(Client.class));
	}

	@Test
	public void deleteShouldDeleteExpectedClient() {

		Client mockClient = mock(Client.class);

		when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(mockClient));

		clientService.delete(EXISTS_CLIENT_ID);

		verify(clientRepository).deleteById(EXISTS_CLIENT_ID);
	}

	@Test
	public void deleteShouldThrowEntityNotFoundExceptionWhenClientDoesNotExist() {

		when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> clientService.delete(NOT_EXISTS_CLIENT_ID));

		verify(clientRepository, never()).deleteById(anyLong());
	}
}

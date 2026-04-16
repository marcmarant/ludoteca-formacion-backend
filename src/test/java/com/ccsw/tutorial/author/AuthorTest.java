package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.game.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    public static final Long EXISTS_AUTHOR_ID = 1L;
    public static final Long NOT_EXISTS_AUTHOR_ID = 0L;

    @Test
    public void findAllShouldReturnAllAuthors() {

        List<Author> list = new ArrayList<>();
        list.add(mock(Author.class));

        when(authorRepository.findAll()).thenReturn(list);

        List<Author> authors = authorService.findAll();

        assertNotNull(authors);
        assertEquals(1, authors.size());
    }

    @Test
    public void findPageShouldReturnExpectedPage() {

        AuthorSearchDTO dto = new AuthorSearchDTO();
        dto.setPageable(new PageableRequest(0, 5));

        Page<Author> mockPage = new PageImpl<>(List.of(mock(Author.class)));

        when(authorRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Author> page = authorService.findPage(dto);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        verify(authorRepository).findAll(dto.getPageable().getPageable());
    }

    @Test
    public void findByIdShouldReturnExpectedAuthor() {

        Author author = mock(Author.class);
        when(author.getId()).thenReturn(EXISTS_AUTHOR_ID);
        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(author));

        Author authorResponse = authorService.findById(EXISTS_AUTHOR_ID);

        assertNotNull(authorResponse);

        assertEquals(EXISTS_AUTHOR_ID, authorResponse.getId());
    }

    @Test
    public void findByNotExistingIdShouldThrowEntityNotFoundException() {

        when(authorRepository.findById(NOT_EXISTS_AUTHOR_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.findById(NOT_EXISTS_AUTHOR_ID));
    }

    @Test
    public void createShouldCreateAnAuthor() {

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);

        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
            Author author = invocation.getArgument(0);
            author.setId(EXISTS_AUTHOR_ID);
            return author;
        });

        AuthorDTO dto = new AuthorDTO();
        dto.setName("Test Author");
        dto.setNationality("ES");
        authorService.create(dto);

        verify(authorRepository).save(authorCaptor.capture());

        Author savedAuthor = authorCaptor.getValue();

        assertEquals(EXISTS_AUTHOR_ID, savedAuthor.getId());
        assertEquals(dto.getName(), savedAuthor.getName());
        assertEquals(dto.getNationality(), savedAuthor.getNationality());
    }

    @Test
    public void updateShouldReplaceExpectedAuthor() {

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);

        Author existingAuthor = new Author();
        existingAuthor.setId(EXISTS_AUTHOR_ID);
        existingAuthor.setName("Old Name");
        existingAuthor.setNationality("FR");

        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthorDTO dto = new AuthorDTO();
        dto.setId(EXISTS_AUTHOR_ID);
        dto.setName("Updated Name");
        dto.setNationality("ES");

        authorService.update(dto);

        verify(authorRepository).save(authorCaptor.capture());

        Author savedAuthor = authorCaptor.getValue();

        assertEquals(dto.getId(), savedAuthor.getId());
        assertEquals(dto.getName(), savedAuthor.getName());
        assertEquals(dto.getNationality(), savedAuthor.getNationality());
    }

    @Test
    public void updateNonExistentAuthorShouldThrowEntityNotFoundException() {

        AuthorDTO dto = new AuthorDTO();
        dto.setId(NOT_EXISTS_AUTHOR_ID);
        dto.setName("Updated Name");
        dto.setNationality("ES");

        when(authorRepository.findById(NOT_EXISTS_AUTHOR_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.update(dto));

        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    public void deleteShouldDeleteExpectedAuthor() {

        Author mockAuthor = mock(Author.class);

        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(mockAuthor));

        when(gameRepository.existsByAuthor_Id(EXISTS_AUTHOR_ID)).thenReturn(false);

        authorService.delete(EXISTS_AUTHOR_ID);

        verify(authorRepository).deleteById(EXISTS_AUTHOR_ID);
    }

    @Test
    public void deleteNonExistentAuthorShouldThrowEntityNotFoundException() {

        when(authorRepository.findById(NOT_EXISTS_AUTHOR_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> authorService.delete(NOT_EXISTS_AUTHOR_ID));

        assertEquals("Author " + NOT_EXISTS_AUTHOR_ID + " not found", exception.getMessage());
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    public void deleteReferencedAuthorShouldThrowDeleteEntityConflictException() {

        Author mockAuthor = mock(Author.class);

        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(mockAuthor));

        when(gameRepository.existsByAuthor_Id(EXISTS_AUTHOR_ID)).thenReturn(true);

        assertThrows(DeleteEntityConflictException.class, () -> authorService.delete(EXISTS_AUTHOR_ID));
        verify(authorRepository, never()).deleteById(anyLong());
    }
}

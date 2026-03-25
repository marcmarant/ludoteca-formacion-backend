package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    // ESTOS TESTS NO TIENEN SENTIDO
    /*@Test
    public void findPageShouldReturnAPageOfAuthors() {

        Page<Author> mockPage = mock(Page.class);

        when(authorRepository.findAll()).thenReturn(mockPage);

        Page<Author> page = authorService.findPage();

        assertNotNull(page);
    }*/

    /*@Test
    public void findByIdShouldReturnExpectedCategory() {

        Category mockCategory = mock(Category.class);

        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(mockCategory));

        Category category = categoryService.findById((long) 1);

        assertEquals(mockCategory, category);
    }

    @Test
    public void createShouldCreateAValidCategory() {

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId((long) 1); // Simulate the auto-generated ID
            return category;
        });

        CategoryDTO dto = new CategoryDTO();
        dto.setName("Test Category");
        categoryService.create(dto);

        verify(categoryRepository).save(categoryCaptor.capture());

        Category savedCategory = categoryCaptor.getValue();

        assertEquals(1, savedCategory.getId());
        assertEquals(dto.getName(), savedCategory.getName());
    }

    @Test
    public void createShouldDenyAnInvalidCategory() {

        when(categoryRepository.save(argThat(category -> category.getName() == null || category.getName().isBlank())))
                .thenThrow(new IllegalArgumentException("Name cannot be blank"));

        CategoryDTO dto = new CategoryDTO(); // Invalid name (null)

        assertThrows(IllegalArgumentException.class, () -> categoryService.create(dto));

        dto.setName(""); // Invalid name (NotBlank)

        assertThrows(IllegalArgumentException.class, () -> categoryService.create(dto));
    }

    @Test
    public void updateShouldReplaceACategory() {

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId((long) 3);
            return category;
        });

        CategoryDTO dto = new CategoryDTO();
        dto.setId((long) 3);
        dto.setName("Updated Category");

        when(categoryRepository.findById(dto.getId())).thenReturn(Optional.of(new Category()));

        categoryService.update(dto);

        verify(categoryRepository).save(categoryCaptor.capture());

        Category savedCategory = categoryCaptor.getValue();

        assertEquals(dto.getId(), savedCategory.getId());
        assertEquals(dto.getName(), savedCategory.getName());
    }

    @Test
    public void deleteShouldDeleteExpectedCategory() {

        Category mockCategory = mock(Category.class);

        when(categoryRepository.findById((long) 1)).thenReturn(Optional.of(mockCategory));

        categoryService.delete((long) 1);

        verify(categoryRepository).deleteById((long) 1);
    }*/
}

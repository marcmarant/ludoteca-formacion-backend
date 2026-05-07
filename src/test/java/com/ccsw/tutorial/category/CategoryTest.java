package com.ccsw.tutorial.category;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import com.ccsw.tutorial.game.GameRepository;
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
public class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    public static final Long EXISTS_CATEGORY_ID = 1L;
    public static final Long NOT_EXISTS_CATEGORY_ID = 7L;

    @Test
    public void findAllShouldReturnAllCategories() {

        List<Category> list = new ArrayList<>();
        list.add(mock(Category.class));

        when(categoryRepository.findAll()).thenReturn(list);

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
    }

    @Test
    public void findByIdShouldReturnExpectedCategory() {

        Category mockCategory = mock(Category.class);

        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(mockCategory));

        Category category = categoryService.findById(EXISTS_CATEGORY_ID);

        assertEquals(mockCategory, category);
    }

    @Test
    public void findByNotExistingIdShouldThrowEntityNotFoundException() {

        when(categoryRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(NOT_EXISTS_CATEGORY_ID));
    }

    @Test
    public void createShouldCreateAValidCategory() {

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(EXISTS_CATEGORY_ID);
            return category;
        });

        CategoryDTO dto = new CategoryDTO();
        dto.setName("Test Category");
        categoryService.create(dto);

        verify(categoryRepository).save(categoryCaptor.capture());

        Category savedCategory = categoryCaptor.getValue();

        assertEquals(EXISTS_CATEGORY_ID, savedCategory.getId());
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
            category.setId(EXISTS_CATEGORY_ID);
            return category;
        });

        CategoryDTO dto = new CategoryDTO();
        dto.setId(EXISTS_CATEGORY_ID);
        dto.setName("Updated Category");

        when(categoryRepository.findById(dto.getId())).thenReturn(Optional.of(new Category()));

        categoryService.update(dto);

        verify(categoryRepository).save(categoryCaptor.capture());

        Category savedCategory = categoryCaptor.getValue();

        assertEquals(dto.getId(), savedCategory.getId());
        assertEquals(dto.getName(), savedCategory.getName());
    }

    @Test
    public void updateNonExistentCategoryShouldThrowEntityNotFoundException() {

        CategoryDTO dto = new CategoryDTO();
        dto.setId(NOT_EXISTS_CATEGORY_ID);
        dto.setName("Updated Name");

        when(categoryRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(dto));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void deleteShouldDeleteExpectedCategory() {

        Category mockCategory = mock(Category.class);

        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(mockCategory));

        when(gameRepository.existsByCategoryId(EXISTS_CATEGORY_ID)).thenReturn(false);

        categoryService.delete(EXISTS_CATEGORY_ID);

        verify(categoryRepository).deleteById(EXISTS_CATEGORY_ID);
    }

    @Test
    public void deleteNonExistentCategoryShouldThrowEntityNotFoundException() {

        when(categoryRepository.findById(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> categoryService.delete(NOT_EXISTS_CATEGORY_ID));

        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    public void deleteReferencedCategoryShouldThrowDeleteEntityConflictException() {

        Category mockCategory = mock(Category.class);

        when(categoryRepository.findById(EXISTS_CATEGORY_ID)).thenReturn(Optional.of(mockCategory));

        when(gameRepository.existsByCategoryId(EXISTS_CATEGORY_ID)).thenReturn(true);

        assertThrows(DeleteEntityConflictException.class, () -> categoryService.delete(EXISTS_CATEGORY_ID));
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}

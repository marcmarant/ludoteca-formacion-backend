package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameTest {

	@Mock
	private GameRepository gameRepository;

	@Mock
	private AuthorService authorService;

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private GameServiceImpl gameService;

	public static final Long EXISTS_GAME_ID = 1L;
	public static final Long NOT_EXISTS_GAME_ID = 7L;
	public static final Long EXISTS_AUTHOR_ID = 1L;
	public static final Long EXISTS_CATEGORY_ID = 1L;

    private GameDTO createGameDTO(Long id, String title, String age, Long authorId, Long categoryId) {

        GameDTO dto = new GameDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setAge(age);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(authorId);
        dto.setAuthor(authorDTO);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);
        dto.setCategory(categoryDTO);

        return dto;
    }

	@Test
	public void findShouldReturnExpectedGames() {

		List<Game> list = new ArrayList<>();
		list.add(new Game());

		when(gameRepository.findAll(argThat((Specification<Game> spec) -> spec != null))).thenReturn(list);

		List<Game> games = gameService.find("test", EXISTS_CATEGORY_ID);

		assertNotNull(games);
		assertEquals(1, games.size());
	}

	@Test
	public void createShouldCreateAValidGame() {

		ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

		Author author = new Author();
		author.setId(EXISTS_AUTHOR_ID);

		Category category = new Category();
		category.setId(EXISTS_CATEGORY_ID);

		when(authorService.findById(EXISTS_AUTHOR_ID)).thenReturn(author);
		when(categoryService.findById(EXISTS_CATEGORY_ID)).thenReturn(category);
		when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

		GameDTO dto = createGameDTO(null, "Test Game", "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		gameService.create(dto);

		verify(gameRepository).save(gameCaptor.capture());

		Game savedGame = gameCaptor.getValue();

		assertEquals(dto.getTitle(), savedGame.getTitle());
		assertEquals(dto.getAge(), savedGame.getAge());
		assertEquals(EXISTS_AUTHOR_ID, savedGame.getAuthor().getId());
		assertEquals(EXISTS_CATEGORY_ID, savedGame.getCategory().getId());
	}

	@Test
	public void createWithANonExistentAUthorShouldThrowEntityNotFoundException() {

		when(authorService.findById(EXISTS_AUTHOR_ID)).thenThrow(new EntityNotFoundException());

		GameDTO dto = createGameDTO(null, "Test Game", "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		assertThrows(EntityNotFoundException.class, () -> gameService.create(dto));

		verify(gameRepository, never()).save(any(Game.class));
	}

	@Test
	public void createWithANonExistentCategoryShouldThrowEntityNotFoundException() {

		Author author = new Author();
		author.setId(EXISTS_AUTHOR_ID);

		when(authorService.findById(EXISTS_AUTHOR_ID)).thenReturn(author);
		when(categoryService.findById(EXISTS_CATEGORY_ID)).thenThrow(new EntityNotFoundException());

		GameDTO dto = createGameDTO(null, "Test Game", "18", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		assertThrows(EntityNotFoundException.class, () -> gameService.create(dto));

		verify(gameRepository, never()).save(any(Game.class));
	}

	@Test
	public void updateShouldReplaceExpectedGame() {

		ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

		Game existingGame = new Game();
		existingGame.setId(EXISTS_GAME_ID);
		existingGame.setTitle("Old Title");
		existingGame.setAge("12");

		Author author = new Author();
		author.setId(EXISTS_AUTHOR_ID);

		Category category = new Category();
		category.setId(EXISTS_CATEGORY_ID);

		when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(existingGame));
		when(authorService.findById(EXISTS_AUTHOR_ID)).thenReturn(author);
		when(categoryService.findById(EXISTS_CATEGORY_ID)).thenReturn(category);
		when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

		GameDTO dto = createGameDTO(EXISTS_GAME_ID, "Updated Title", "16", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		gameService.update(dto);

		verify(gameRepository).save(gameCaptor.capture());

		Game savedGame = gameCaptor.getValue();

		assertEquals(EXISTS_GAME_ID, savedGame.getId());
		assertEquals(dto.getTitle(), savedGame.getTitle());
		assertEquals(dto.getAge(), savedGame.getAge());
		assertEquals(EXISTS_AUTHOR_ID, savedGame.getAuthor().getId());
		assertEquals(EXISTS_CATEGORY_ID, savedGame.getCategory().getId());
	}

	@Test
	public void updateANonExistentGameShouldThrowEntityNotFoundException() {

		when(gameRepository.findById(NOT_EXISTS_GAME_ID)).thenReturn(Optional.empty());

		GameDTO dto = createGameDTO(NOT_EXISTS_GAME_ID, "Updated Title", "16", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		assertThrows(EntityNotFoundException.class, () -> gameService.update(dto));

		verify(gameRepository, never()).save(any(Game.class));
	}

	@Test
	public void updateWithANonExistentAuthorShouldThrowEntityNotFoundException() {

		Game existingGame = new Game();
		existingGame.setId(EXISTS_GAME_ID);

		when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(existingGame));
		when(authorService.findById(EXISTS_AUTHOR_ID)).thenThrow(new EntityNotFoundException());

		GameDTO dto = createGameDTO(EXISTS_GAME_ID, "Updated Title", "16", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		assertThrows(EntityNotFoundException.class, () -> gameService.update(dto));

		verify(gameRepository, never()).save(any(Game.class));
	}

	@Test
	public void updateWithANonExistentCategoryShouldThrowEntityNotFoundException() {

		Game existingGame = new Game();
		existingGame.setId(EXISTS_GAME_ID);

		Author author = new Author();
		author.setId(EXISTS_AUTHOR_ID);

		when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(existingGame));
		when(authorService.findById(EXISTS_AUTHOR_ID)).thenReturn(author);
		when(categoryService.findById(EXISTS_CATEGORY_ID)).thenThrow(new EntityNotFoundException());

		GameDTO dto = createGameDTO(EXISTS_GAME_ID, "Updated Title", "16", EXISTS_AUTHOR_ID, EXISTS_CATEGORY_ID);

		assertThrows(EntityNotFoundException.class, () -> gameService.update(dto));

		verify(gameRepository, never()).save(any(Game.class));
	}
}

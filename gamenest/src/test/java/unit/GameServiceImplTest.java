package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GameMapper;
import com.gamenest.model.Game;
import com.gamenest.model.User;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.repository.GameRepository;
import com.gamenest.repository.TagRepository;
import com.gamenest.repository.UserRepository;
import com.gamenest.service.implementation.GameServiceImpl;
import com.gamenest.service.interfaces.GhRepositoryService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private GhRepositoryService ghRepositoryService;

    private GameServiceImpl gameService;

    private User testUser;
    private Game testGame;
    private GameRequest testGameRequest;
    private UpdateGameRequest testUpdateGameRequest;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(gameRepository, userRepository, categoryRepository, tagRepository, gameMapper,
                ghRepositoryService, eventPublisher);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testGame = Game.builder()
                .id(1L)
                .title("Test Game")
                .description("A test game")
                .version("1.0")
                .owner(testUser)
                .build();

        testGameRequest = GameRequest.builder()
                .id(1L)
                .title("Test Game")
                .description("A test game")
                .version("1.0")
                .build();

        testUpdateGameRequest = UpdateGameRequest.builder()
                .title("Updated Test Game")
                .description("An updated test game")
                .version("1.1")
                .build();

        // Set up SecurityContextHolder with a dummy authentication
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createGame_WhenUserExists_ShouldCreateAndReturnDTO() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(gameMapper.convertToEntity(testGameRequest)).thenReturn(testGame);
        when(gameRepository.save(testGame)).thenReturn(testGame);
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        GameRequest result = gameService.createGame(testGameRequest);

        assertNotNull(result);
        assertEquals(testGameRequest.getTitle(), result.getTitle());
        verify(userRepository).findByUsername("testuser");
        verify(gameRepository).save(testGame);
    }

    @Test
    void createGame_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        assertThrows(ResourceNotFoundException.class, () -> gameService.createGame(testGameRequest));
        verify(userRepository).findByUsername("testuser");
        verify(gameRepository, never()).save(any());
    }

    @Test
    void updateGame_WhenGameExists_ShouldUpdateAndReturnDTO() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(gameRepository.save(testGame)).thenReturn(testGame);
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        GameRequest result = gameService.updateGame(1L, testUpdateGameRequest);

        assertNotNull(result);
        verify(gameRepository).findById(1L);
        verify(gameRepository).save(testGame);
    }

    @Test
    void updateGame_WhenGameDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.updateGame(1L, testUpdateGameRequest));
        verify(gameRepository).findById(1L);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void getGameById_WhenGameExists_ShouldReturnDTO() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        GameRequest result = gameService.getGameById(1L);

        assertNotNull(result);
        verify(gameRepository).findById(1L);
    }

    @Test
    void getGameById_WhenGameDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameById(1L));
        verify(gameRepository).findById(1L);
    }

    @Test
    void getAllGames_ShouldReturnListOfDTOs() {
        List<Game> gameList = List.of(testGame);
        when(gameRepository.findAll()).thenReturn(gameList);
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        List<GameRequest> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameRepository).findAll();
    }

    @Test
    void deleteGame_WhenGameExists_ShouldDeleteGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));

        gameService.deleteGame(1L);

        verify(gameRepository).findById(1L);
        verify(gameRepository).delete(testGame);
    }

    @Test
    void deleteGame_WhenGameDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame(1L));
        verify(gameRepository).findById(1L);
        verify(gameRepository, never()).delete(any());
    }
}

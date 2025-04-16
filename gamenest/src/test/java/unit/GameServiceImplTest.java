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
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.events.GameBuildEvent;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GameMapper;
import com.gamenest.model.Build;
import com.gamenest.model.Game;
import com.gamenest.model.GhRepository;
import com.gamenest.model.User;
import com.gamenest.model.enums.BuildStatus;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
    private Build testBuild;
    private GameRequest testGameRequest;
    private GhRepositoryRequest testRepoRequest;
    private GhRepository testGhRepository;

    private UpdateGameRequest testUpdateGameRequest;

    @Captor
    private ArgumentCaptor<Game> gameArgumentCaptor;
    @Captor
    private ArgumentCaptor<GameBuildEvent> eventArgumentCaptor;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(gameRepository, userRepository, categoryRepository, tagRepository, gameMapper,
                ghRepositoryService, eventPublisher);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testBuild = new Build();
        testBuild.setBuildStatus(BuildStatus.SUCCESS);

        testRepoRequest = GhRepositoryRequest.builder()
                .ghId(12345L) // Example GitHub repo ID
                .name("Test-Repo")
                .htmlUrl("https://github.com/user/Test-Repo")
                .build();

        testGhRepository = GhRepository.builder()
                .ghId(12345L) // Example GitHub repo ID
                .name("Test-Repo")
                .htmlUrl("https://github.com/user/Test-Repo")
                .build();

        testGame = Game.builder()
                .id(1L)
                .title("Test Game")
                .description("A test game")
                .version("1.0")
                .builds(List.of(testBuild))
                .owner(testUser)
                .repository(testGhRepository)
                .build();

        testGameRequest = GameRequest.builder()
                .id(1L)
                .title("Test Game")
                .description("A test game")
                .version("1.0")
                .repository(testRepoRequest)
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
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        when(gameMapper.convertToEntity(testGameRequest)).thenReturn(testGame);
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        when(ghRepositoryService.getRepositoryByGhId(testRepoRequest.getGhId())).thenReturn(null);
        when(ghRepositoryService.createRepository(testRepoRequest)).thenReturn(testGhRepository);

        when(gameRepository.save(testGame)).thenReturn(testGame);

        // Mock event publish
        doNothing().when(eventPublisher).publishEvent(any(GameBuildEvent.class));

        GameRequest result = gameService.createGame(testGameRequest);

        assertEquals(testGameRequest.getTitle(), result.getTitle());
        verify(userRepository).findByUsername("testuser");
        verify(gameRepository).save(testGame);

        verify(userRepository).findByUsername("testuser");
        verify(gameMapper).convertToEntity(testGameRequest);
        verify(ghRepositoryService).getRepositoryByGhId(testRepoRequest.getGhId());
        verify(ghRepositoryService).createRepository(testRepoRequest);

        verify(gameRepository).save(gameArgumentCaptor.capture());
        Game capturedGame = gameArgumentCaptor.getValue();
        assertNotNull(capturedGame.getOwner(), "Owner should be set before saving");
        assertEquals(testUser.getId(), capturedGame.getOwner().getId(), "Correct owner ID should be set");
        assertNotNull(capturedGame.getRepository(), "Repository should be set before saving");
        assertEquals(testGhRepository.getId(), capturedGame.getRepository().getId(),
                "Correct repository ID should be set");

        verify(eventPublisher).publishEvent(eventArgumentCaptor.capture());
        GameBuildEvent capturedEvent = eventArgumentCaptor.getValue();
        assertEquals(testGame, capturedEvent.getGame(), "Event should contain the saved game entity");

        verify(gameMapper).convertToDTO(testGame);
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
        when(gameMapper.convertToDTO(testGame, "repository", "category", "tags")).thenReturn(testGameRequest);

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
        when(gameRepository.findFiltered()).thenReturn(gameList);
        when(gameMapper.convertToDTO(testGame)).thenReturn(testGameRequest);

        List<GameRequest> result = gameService.getAllGames();

        assertNotNull(result);
        // It will be filtered out because there is no successful build associated with
        // it
        assertEquals(1, result.size());
        verify(gameRepository).findFiltered();
    }

    @Test
    void deleteGame_WhenGameExists_ShouldDeleteGame() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));

        gameService.deleteGame(1L);

        verify(gameRepository).findById(1L);
        verify(gameRepository).save(testGame);
    }

    @Test
    void deleteGame_WhenGameDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame(1L));
        verify(gameRepository).findById(1L);
        verify(gameRepository, never()).delete(any());
    }
}

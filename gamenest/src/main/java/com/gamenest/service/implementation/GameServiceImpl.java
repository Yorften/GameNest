package com.gamenest.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.events.GameBuildEvent;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GameMapper;
import com.gamenest.model.Category;
import com.gamenest.model.Game;
import com.gamenest.model.GhRepository;
import com.gamenest.model.Tag;
import com.gamenest.model.User;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.repository.GameRepository;
import com.gamenest.repository.TagRepository;
import com.gamenest.repository.UserRepository;
import com.gamenest.service.interfaces.GameService;
import com.gamenest.service.interfaces.GhRepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for Game entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final GameMapper gameMapper;
    private final GhRepositoryService ghRepositoryService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public GameRequest createGame(GameRequest gameRequest) {
        User owner = getCurrentUser();

        Game game = gameMapper.convertToEntity(gameRequest);
        game.setOwner(owner);

        GhRepositoryRequest repoReq = gameRequest.getRepository();
        GhRepository ghRepo = ghRepositoryService.createRepository(repoReq);
        game.setRepository(ghRepo);

        game = gameRepository.save(game);
        eventPublisher.publishEvent(new GameBuildEvent(this, game));
        return gameMapper.convertToDTO(game);
    }

    @Override
    public GameRequest updateGame(Long gameId, UpdateGameRequest updateGameRequest) {
        // log.info("Game id: {}", gameId);
        // log.info("Game update request: {}", updateGameRequest);
        boolean shouldBuild = false;
        Game gameDB = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        gameDB.setTitle(optionalOverwrite(updateGameRequest.getTitle(), gameDB.getTitle()));
        gameDB.setDescription(optionalOverwrite(updateGameRequest.getDescription(), gameDB.getDescription()));
        gameDB.setVersion(optionalOverwrite(updateGameRequest.getVersion(), gameDB.getVersion()));

        if (updateGameRequest.getRepository() != null) {
            GhRepositoryRequest repositoryRequest = updateGameRequest.getRepository();

            GhRepository currentRepo = gameDB.getRepository();

            if (currentRepo == null) {
                // No repository exists yet, so create the new one
                GhRepository newRepo = ghRepositoryService.createRepository(repositoryRequest);
                gameDB.setRepository(newRepo);
                shouldBuild = true;
            } else if (!currentRepo.getGhId().equals(repositoryRequest.getGhId())) {
                // The repositories are different:
                // Delete the old repository, then create and set the new one.
                gameDB.setRepository(null);
                ghRepositoryService.deleteRepository(currentRepo.getId());
                GhRepository newRepo = ghRepositoryService.createRepository(repositoryRequest);
                gameDB.setRepository(newRepo);
                shouldBuild = true;
            }
        }

        if (updateGameRequest.getCategory() != null && updateGameRequest.getCategory().getName() != null) {
            Category newCat = categoryRepository.findByName(updateGameRequest.getCategory().getName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found: " + updateGameRequest.getCategory().getName()));
            gameDB.setCategory(newCat);
        }

        if (updateGameRequest.getTags() != null) {
            gameDB.getTags().clear();
            Set<Tag> newTags = updateGameRequest.getTags().stream()
                    .map(tagReq -> tagRepository.findByName(tagReq.getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagReq.getName())))
                    .collect(Collectors.toSet());
            gameDB.setTags(newTags);
        }
        Game game = gameRepository.save(gameDB);
        if (shouldBuild)
            eventPublisher.publishEvent(new GameBuildEvent(this, game));
        return gameMapper.convertToDTO(game, "repository", "category", "tags");
    }

    @Override
    public GameRequest getGameById(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        return gameMapper.convertToDTO(game);
    }

    @Override
    public GameRequest getGameById(Long gameId, String... with) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        return gameMapper.convertToDTO(game, with);
    }

    @Override
    public List<GameRequest> getAllGames() {
        return gameRepository.findFiltered().stream()
                .map(gameMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameRequest> getAllGames(String... with) {
        return gameRepository.findFiltered().stream()
                .map(game -> gameMapper.convertToDTO(game, with))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameRequest> getAllGamesFiltered(Long categoryId, List<Long> tagIds) {
        if (categoryId == null && (tagIds == null || tagIds.isEmpty())) {
            return getAllGames();
        }

        List<Game> filteredGames = gameRepository.findFiltered(categoryId, tagIds);

        return filteredGames.stream()
                .map(gameMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameRequest> getAllGamesFiltered(Long categoryId, List<Long> tagIds, String... with) {
        if (categoryId == null && (tagIds == null || tagIds.isEmpty())) {
            return getAllGames(with);
        }

        List<Game> filteredGames = gameRepository.findFiltered(categoryId, tagIds);

        return filteredGames.stream()
                .map(game -> gameMapper.convertToDTO(game, with))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameRequest> getUserGames(String username, String... with) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return gameRepository.findByOwnerId(user.getId()).stream()
                .map(game -> gameMapper.convertToDTO(game, with))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        game.setDeletedAt(LocalDateTime.now());
        gameRepository.save(game);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String optionalOverwrite(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }
}

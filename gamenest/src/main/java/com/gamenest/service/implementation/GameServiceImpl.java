package com.gamenest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GameMapper;
import com.gamenest.model.Game;
import com.gamenest.model.User;
import com.gamenest.repository.GameRepository;
import com.gamenest.repository.UserRepository;
import com.gamenest.service.interfaces.GameService;

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
    private final GameMapper gameMapper;

    @Override
    public GameRequest createGame(GameRequest gameRequest) {
        User owner = getCurrentUser();

        Game game = gameMapper.convertToEntity(gameRequest);
        game.setOwner(owner);

        game = gameRepository.save(game);
        return gameMapper.convertToDTO(game);
    }

    @Override
    public GameRequest updateGame(Long gameId, UpdateGameRequest updateGameRequest) {
        Game gameDB = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

        if (updateGameRequest.getTitle() != null && !updateGameRequest.getTitle().isEmpty()) {
            gameDB.setTitle(updateGameRequest.getTitle());
        }
        if (updateGameRequest.getDescription() != null && !updateGameRequest.getDescription().isEmpty()) {
            gameDB.setDescription(updateGameRequest.getDescription());
        }
        if (updateGameRequest.getVersion() != null && !updateGameRequest.getVersion().isEmpty()) {
            gameDB.setVersion(updateGameRequest.getVersion());
        }
        if (updateGameRequest.getPath() != null && !updateGameRequest.getPath().isEmpty()) {
            gameDB.setPath(updateGameRequest.getPath());
        }
        if (updateGameRequest.getRepositoryName() != null && !updateGameRequest.getRepositoryName().isEmpty()) {
            gameDB.setRepositoryName(updateGameRequest.getRepositoryName());
        }
        if (updateGameRequest.getPrivateRepository() != null) {
            gameDB.setPrivateRepository(updateGameRequest.getPrivateRepository());
        }

        Game updatedGame = gameRepository.save(gameDB);
        return gameMapper.convertToDTO(updatedGame);
    }

    @Override
    public GameRequest getGameById(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        return gameMapper.convertToDTO(game);
    }

    @Override
    public List<GameRequest> getAllGames() {
        return gameRepository.findAll().stream()
                .map(gameMapper::convertToDTO)
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
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        gameRepository.delete(game);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

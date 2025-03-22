package com.gamenest.service.interfaces;

import java.util.List;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;

/**
 * Service interface for Game entity.
 * Defines methods for CRUD operations and additional business logic.
 */
public interface GameService {

    GameRequest createGame(GameRequest gameRequest);

    GameRequest updateGame(Long gameId, UpdateGameRequest gameRequest);

    GameRequest getGameById(Long gameId);

    GameRequest getGameById(Long gameId, String... with);

    List<GameRequest> getAllGames();

    List<GameRequest> getAllGames(String... with);

    List<GameRequest> getAllGamesFiltered(Long categoryId, List<Long> tagIds);

    List<GameRequest> getAllGamesFiltered(Long categoryId, List<Long> tagIds, String... with);

    List<GameRequest> getUserGames(String username, String... with);

    void deleteGame(Long gameId);

}

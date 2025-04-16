package com.gamenest.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.ResourceOwnershipException;
import com.gamenest.service.interfaces.GameService;
import com.gamenest.service.interfaces.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/games")
@Tag(name = "Game API", description = "Operations pertaining to games in the application")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @Operation(summary = "Create a new game", description = "Creates a new game and associates it with the currently authenticated user.")
    @PostMapping
    public ResponseEntity<GameRequest> createGame(@RequestBody @Valid GameRequest gameRequest) {
        GameRequest createdGame = gameService.createGame(gameRequest);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing game", description = "Updates the fields of an existing game. Only provided fields are updated.")
    @PutMapping("/{gameId}")
    public ResponseEntity<GameRequest> updateGame(
            @PathVariable Long gameId,
            @RequestBody UpdateGameRequest updateGameRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);
        GameRequest game = gameService.getGameById(gameId);
        if (game.getOwner().getId() != user.getId())
            throw new ResourceOwnershipException("You are not the owner of this resource");

        GameRequest updatedGame = gameService.updateGame(gameId, updateGameRequest);
        return ResponseEntity.ok(updatedGame);
    }

    @Operation(summary = "Get a game by ID", description = "Retrieves a single game by its ID.")
    @GetMapping("/{gameId}")
    public ResponseEntity<GameRequest> getGameById(@PathVariable Long gameId) {
        GameRequest game = gameService.getGameById(gameId, "owner", "repository", "category", "tags", "last-build");
        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Get all games", description = "Retrieves a list of all games. Optionally filter by category or tags.")
    @GetMapping
    public ResponseEntity<List<GameRequest>> getAllGames(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> tagIds) {

        List<GameRequest> games = gameService.getAllGamesFiltered(categoryId, tagIds, "owner", "category", "tags",
                "last-build");
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "Delete a game", description = "Deletes a game by its ID.")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);
        GameRequest game = gameService.getGameById(gameId);
        if (game.getOwner().getId() != user.getId())
            throw new ResourceOwnershipException("You are not the owner of this resource");

        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }
}

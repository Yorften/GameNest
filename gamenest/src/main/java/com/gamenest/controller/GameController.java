package com.gamenest.controller;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.game.UpdateGameRequest;
import com.gamenest.service.interfaces.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Game API", description = "Operations pertaining to games in the application")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

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
        GameRequest updatedGame = gameService.updateGame(gameId, updateGameRequest);
        return ResponseEntity.ok(updatedGame);
    }

    @Operation(summary = "Get a game by ID", description = "Retrieves a single game by its ID.")
    @GetMapping("/{gameId}")
    public ResponseEntity<GameRequest> getGameById(@PathVariable Long gameId) {
        GameRequest game = gameService.getGameById(gameId);
        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Get all games", description = "Retrieves a list of all games.")
    @GetMapping
    public ResponseEntity<List<GameRequest>> getAllGames() {
        List<GameRequest> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "Delete a game", description = "Deletes a game by its ID.")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }
}

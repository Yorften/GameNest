package com.gamenest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.service.interfaces.UserService;

/**
 * REST controller for managing User entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "APIs for managing user accounts and roles")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all users with their roles.")
    public ResponseEntity<List<UserRequest>> getAllUsers() {
        List<UserRequest> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get current user info", description = "Retrieves information about the currently authenticated user.")
    @GetMapping("/@me")
    public ResponseEntity<UserRequest> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user installation id", description = "Retrieves information about the currently authenticated user.")
    @PostMapping("/installation/{installationId}")
    public ResponseEntity<UserRequest> updateUserInstallation(@PathVariable Long installationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserRequest user =userService.updateUserInstallation(installationId, username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get current user's games", description = "Retrieves all games belonging to the authenticated user.")
    @GetMapping("/games")
    public ResponseEntity<List<GameRequest>> getUserGames() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GameRequest> userGames = userService.getUserGames(username);
        return ResponseEntity.ok(userGames);
    }

}

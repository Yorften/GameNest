package com.gamenest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GHAppInstallationToken;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
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
import lombok.extern.slf4j.Slf4j;

import com.gamenest.config.github.JwtTokenUtil;
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
@Slf4j

public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${github.webhook.app-id}")
    private String applicationId;

    @Operation(summary = "Get user repositories", description = "Retrieves information about the user's github reposiories.")
    @GetMapping("/repositories")
    public ResponseEntity<List<Map<String, Object>>> getUserRepositories() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);

        String jwtToken = jwtTokenUtil.createJWT(applicationId, 60000);

        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();

        GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationById(user.getInstallationId());
        GHAppInstallationToken appInstallationToken = appInstallation.createToken().create();

        GitHub installationClient = new GitHubBuilder().withAppInstallationToken(appInstallationToken.getToken())
                .build();

        log.info("Github app token: {}", appInstallationToken.getToken());

        List<GHRepository> repositories = installationClient.getInstallation().listRepositories().toList();

        List<Map<String, Object>> simpleRepos = repositories.stream()
                .map(repo -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", repo.getId());
                    map.put("name", repo.getName());
                    map.put("fullName", repo.getFullName());
                    map.put("htmlUrl", repo.getHtmlUrl());
                    map.put("language", repo.getLanguage());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(simpleRepos);
    }

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

        UserRequest user = userService.updateUserInstallation(installationId, username);
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

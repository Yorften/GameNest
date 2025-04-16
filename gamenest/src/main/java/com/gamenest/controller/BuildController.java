package com.gamenest.controller;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.ResourceOwnershipException;
import com.gamenest.service.interfaces.BuildService;
import com.gamenest.service.interfaces.GameService;
import com.gamenest.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/builds")
@Tag(name = "Builds", description = "APIs for retrieving game build information")
public class BuildController {

    private final BuildService buildService;
    private final GameService gameService;
    private final UserService userService;

    @Operation(summary = "Get latest successful build by game id", description = "Retrieves the latest successful build for the specified game.")
    @GetMapping("/game/{gameId}/latest-success")
    public ResponseEntity<BuildRequest> getLatestSuccessfulBuild(@PathVariable Long gameId) {
        BuildRequest latestBuild = buildService.getLatestSuccessfulBuildByGameId(gameId);
        return ResponseEntity.ok(latestBuild);
    }

    @Operation(summary = "Get all builds by game id", description = "Retrieves all builds for the specified game, ordered by creation time descending.")
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<BuildRequest>> getBuildsByGameId(@PathVariable Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserRequest user = userService.getByUserName(username);
        GameRequest game = gameService.getGameById(gameId, "owner");
        if (game.getOwner().getId() != user.getId())
            throw new ResourceOwnershipException("You are not the owner of this resource");

        List<BuildRequest> builds = buildService.getBuildsByGameId(gameId);
        return ResponseEntity.ok(builds);
    }
}

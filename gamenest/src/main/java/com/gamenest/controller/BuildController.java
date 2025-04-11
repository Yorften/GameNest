package com.gamenest.controller;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.service.interfaces.BuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/builds")
@RequiredArgsConstructor
@Tag(name = "Builds", description = "APIs for retrieving game build information")
public class BuildController {

    private final BuildService buildService;

    @Operation(summary = "Get latest successful build by game id", description = "Retrieves the latest successful build for the specified game.")
    @GetMapping("/game/{gameId}/latest-success")
    public ResponseEntity<BuildRequest> getLatestSuccessfulBuild(@PathVariable Long gameId) {
        BuildRequest latestBuild = buildService.getLatestSuccessfulBuildByGameId(gameId);
        return ResponseEntity.ok(latestBuild);
    }

    @Operation(summary = "Get all builds by game id", description = "Retrieves all builds for the specified game, ordered by creation time descending.")
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<BuildRequest>> getBuildsByGameId(@PathVariable Long gameId) {
        List<BuildRequest> builds = buildService.getBuildsByGameId(gameId);
        return ResponseEntity.ok(builds);
    }
}

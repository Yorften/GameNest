package com.gamenest.dto.build;

import java.time.LocalDateTime;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.model.enums.BuildStatus;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BuildRequest {
    private Long id;
    private BuildStatus buildStatus;
    private String logs;
    private String path;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GameRequest game;
}
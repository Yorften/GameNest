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

    @Builder.Default
    private BuildStatus buildStatus = BuildStatus.PENDING;

    private String logs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    private GameRequest game;
}
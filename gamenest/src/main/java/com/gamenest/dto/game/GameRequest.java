package com.gamenest.dto.game;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GameRequest {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Version is required")
    private String version;

    // Git url
    private String url;

    @NotBlank(message = "Repository name is required")
    private String nameSpace;

    private boolean privateRepository;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

package com.gamenest.dto.game;

import java.time.LocalDateTime;
import java.util.Set;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.dto.tag.TagRequest;

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

    @NotBlank(message = "Github repository is required")
    private GhRepositoryRequest repository;

    @NotBlank(message = "Category is required")
    private CategoryRequest category;

    @NotBlank(message = "Tags are required")
    private Set<TagRequest> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

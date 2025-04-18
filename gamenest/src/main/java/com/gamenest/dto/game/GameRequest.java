package com.gamenest.dto.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.user.UserRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Github repository is required")
    private GhRepositoryRequest repository;

    @NotNull(message = "Category is required")
    private CategoryRequest category;

    @NotNull(message = "Tags are required")
    private Set<TagRequest> tags;

    private BuildRequest lastBuild;

    private List<BuildRequest> builds;

    private UserRequest owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

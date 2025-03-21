package com.gamenest.dto.repo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new GhRepository record.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GhRepositoryRequest {

    private Long id;

    @NotBlank(message = "Full name is required")
    private Long ghId;

    @NotBlank(message = "Repository name is required")
    private String name;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Full name is required")
    private String htmlUrl;

    private String language;
}
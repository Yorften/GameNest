package com.gamenest.dto.category;

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
public class CategoryRequest {

    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Category description is required")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
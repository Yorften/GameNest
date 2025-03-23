package com.gamenest.dto.game;

import java.util.Set;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.dto.tag.TagRequest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGameRequest {

    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    private String description;

    private String version;

    private GhRepositoryRequest repository;

    private CategoryRequest category;

    private Set<TagRequest> tags;

}

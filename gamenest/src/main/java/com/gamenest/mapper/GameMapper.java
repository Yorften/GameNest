package com.gamenest.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.tag.TagRequest;
import com.gamenest.model.Category;
import com.gamenest.model.Game;
import com.gamenest.model.Tag;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameMapper {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public Game convertToEntity(GameRequest gameDTO) {
        if (gameDTO == null) {
            return null;
        }

        Category category = null;
        if (gameDTO.getCategory() != null && gameDTO.getCategory().getName() != null) {
            category = categoryRepository.findByName(gameDTO.getCategory().getName())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + gameDTO.getCategory().getName()));
        }

        Set<Tag> tags = null;
        if (gameDTO.getTags() != null && !gameDTO.getTags().isEmpty()) {
            tags = gameDTO.getTags().stream()
                    .map(tagRequest -> tagRepository.findByName(tagRequest.getName())
                            .orElseThrow(() -> new RuntimeException("Tag not found: " + tagRequest.getName())))
                    .collect(Collectors.toSet());
        }

        return Game.builder()
                .id(gameDTO.getId())
                .title(gameDTO.getTitle())
                .description(gameDTO.getDescription())
                .version(gameDTO.getVersion())
                .url(gameDTO.getUrl())
                .nameSpace(gameDTO.getNameSpace())
                .privateRepository(gameDTO.isPrivateRepository())
                .category(category)
                .tags(tags)
                .build();
    }

    public GameRequest convertToDTO(Game game) {
        if (game == null) {
            return null;
        }

        CategoryRequest categoryRequest = null;
        if (game.getCategory() != null) {
            categoryRequest = CategoryRequest.builder()
                    .name(game.getCategory().getName())
                    .build();
        }

        Set<TagRequest> tagRequests = null;
        if (game.getTags() != null && !game.getTags().isEmpty()) {
            tagRequests = game.getTags().stream()
                    .map(tag -> TagRequest.builder().name(tag.getName()).build())
                    .collect(Collectors.toSet());
        }

        return GameRequest.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .version(game.getVersion())
                .url(game.getUrl())
                .nameSpace(game.getNameSpace())
                .privateRepository(game.isPrivateRepository())
                .category(categoryRequest)
                .tags(tagRequests)
                .createdAt(game.getCreatedAt())
                .updatedAt(game.getUpdatedAt())
                .build();
    }

    public List<GameRequest> convertToDTOList(List<Game> games) {
        return games.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

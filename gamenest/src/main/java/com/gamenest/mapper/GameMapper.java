package com.gamenest.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.dto.tag.TagRequest;
import com.gamenest.exception.InvalidDataException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.model.Category;
import com.gamenest.model.Game;
import com.gamenest.model.GhRepository;
import com.gamenest.model.Tag;
import com.gamenest.repository.CategoryRepository;
import com.gamenest.repository.GhRepositoryRepository;
import com.gamenest.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("owner", "category", "repository", "tags", "builds",
            "last-build");

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final GhRepositoryRepository ghRepositoryRepository;

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public Game convertToEntity(GameRequest gameDTO) {
        if (gameDTO == null) {
            return null;
        }

        Category category = null;
        if (gameDTO.getCategory() != null && gameDTO.getCategory().getName() != null) {
            category = categoryRepository.findByName(gameDTO.getCategory().getName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found: " + gameDTO.getCategory().getName()));
        }

        GhRepository ghRepository = null;
        if (gameDTO.getRepository() != null && gameDTO.getRepository().getGhId() != null) {
            ghRepository = ghRepositoryRepository.findByGhId(gameDTO.getRepository().getGhId())
                    .orElseThrow(() -> new ResourceNotFoundException("Repository not found "));
        }

        Set<Tag> tags = null;
        if (gameDTO.getTags() != null && !gameDTO.getTags().isEmpty()) {
            tags = gameDTO.getTags().stream()
                    .map(tagRequest -> tagRepository.findByName(tagRequest.getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagRequest.getName())))
                    .collect(Collectors.toSet());
        }

        return Game.builder()
                .id(gameDTO.getId())
                .title(gameDTO.getTitle())
                .description(gameDTO.getDescription())
                .version(gameDTO.getVersion())
                .repository(ghRepository)
                .category(category)
                .tags(tags)
                .build();
    }

    public GameRequest convertToDTO(Game game) {
        if (game == null) {
            return null;
        }

        return GameRequest.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .version(game.getVersion())
                .createdAt(game.getCreatedAt())
                .updatedAt(game.getUpdatedAt())
                .build();
    }

    public List<GameRequest> convertToDTOList(List<Game> games) {
        return games.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GameRequest convertToDTO(Game game, String... with) {
        verifyIncludes(with);
        List<String> includesList = Arrays.asList(with);

        CategoryRequest categoryRequest = null;
        GhRepositoryRequest ghRepositoryRequest = null;
        Set<TagRequest> tagRequests = null;

        if (game == null) {
            return null;
        }

        if (includesList.contains("category")) {
            if (game.getCategory() != null) {
                categoryRequest = CategoryRequest.builder()
                        .name(game.getCategory().getName())
                        .build();
            }
        }

        if (includesList.contains("repository")) {
            if (game.getRepository() != null) {
                ghRepositoryRequest = GhRepositoryRequest.builder()
                        .id(game.getRepository().getId())
                        .ghId(game.getRepository().getGhId())
                        .name(game.getRepository().getName())
                        .fullName(game.getRepository().getFullName())
                        .htmlUrl(game.getRepository().getHtmlUrl())
                        .language(game.getRepository().getLanguage())
                        .privateRepository(game.getRepository().isPrivateRepository())
                        .build();
            }
        }

        if (includesList.contains("tags")) {
            if (game.getTags() != null && !game.getTags().isEmpty()) {
                tagRequests = game.getTags().stream()
                        .map(tag -> TagRequest.builder().name(tag.getName()).build())
                        .collect(Collectors.toSet());
            }
        }

        if (includesList.contains("builds")) {

        }

        if (includesList.contains("last-build")) {

        }

        if (includesList.contains("owner")) {

        }

        return GameRequest.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .version(game.getVersion())
                .repository(ghRepositoryRequest)
                .category(categoryRequest)
                .tags(tagRequests)
                .createdAt(game.getCreatedAt())
                .updatedAt(game.getUpdatedAt())
                .build();
    }

    public List<GameRequest> convertToDTOList(List<Game> games, String... with) {
        return games.stream()
                .map(game -> convertToDTO(game, with))
                .collect(Collectors.toList());
    }
}

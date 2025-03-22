package com.gamenest.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.category.CategoryRequest;
import com.gamenest.dto.game.GameRequest;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.dto.tag.TagRequest;
import com.gamenest.dto.user.UserRequest;
import com.gamenest.exception.InvalidDataException;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.model.Build;
import com.gamenest.model.Category;
import com.gamenest.model.Game;
import com.gamenest.model.GhRepository;
import com.gamenest.model.Tag;
import com.gamenest.model.User;
import com.gamenest.model.enums.BuildStatus;
import com.gamenest.repository.BuildRepository;
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
    private final BuildRepository buildRepository;
    private final BuildMapper buildMapper;

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
        UserRequest owner = null;
        List<BuildRequest> builds = null;
        BuildRequest lastBuild = null;

        if (game == null) {
            return null;
        }

        if (includesList.contains("category")) {
            if (game.getCategory() != null) {
                Category category = game.getCategory();
                categoryRequest = CategoryRequest.builder()
                        .name(category.getName())
                        .build();
            }
        }

        if (includesList.contains("repository")) {
            if (game.getRepository() != null) {
                GhRepository repository = game.getRepository();
                ghRepositoryRequest = GhRepositoryRequest.builder()
                        .id(repository.getId())
                        .ghId(repository.getGhId())
                        .name(repository.getName())
                        .fullName(repository.getFullName())
                        .htmlUrl(repository.getHtmlUrl())
                        .language(repository.getLanguage())
                        .privateRepository(repository.isPrivateRepository())
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
            if (game.getBuilds() != null && !game.getBuilds().isEmpty()) {
                builds = game.getBuilds().stream()
                        .map(this.buildMapper::convertToDTO)
                        .collect(Collectors.toList());
            }
        }

        if (includesList.contains("last-build")) {
            List<Build> successfulBuilds = buildRepository.findByGame_IdAndBuildStatusOrderByCreatedAtDesc(game.getId(),
                    BuildStatus.SUCCESS);

            if (!successfulBuilds.isEmpty()) {
                lastBuild = buildMapper.convertToDTO(successfulBuilds.get(0));
            }
        }

        if (includesList.contains("owner")) {
            if (game.getCategory() != null) {
                User user = game.getOwner();
                owner = UserRequest.builder()
                        .username(user.getUsername())
                        .build();
            }
        }

        return GameRequest.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .version(game.getVersion())
                .repository(ghRepositoryRequest)
                .category(categoryRequest)
                .tags(tagRequests)
                .builds(builds)
                .lastBuild(lastBuild)
                .owner(owner)
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

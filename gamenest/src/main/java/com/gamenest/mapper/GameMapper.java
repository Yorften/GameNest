package com.gamenest.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gamenest.dto.game.GameRequest;
import com.gamenest.model.Game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameMapper {

    public Game convertToEntity(GameRequest gameDTO) {
        if (gameDTO == null) {
            return null;
        }
        return Game.builder()
                .id(gameDTO.getId())
                .title(gameDTO.getTitle())
                .description(gameDTO.getDescription())
                .version(gameDTO.getVersion())
                .url(gameDTO.getUrl())
                .nameSpace(gameDTO.getNameSpace())
                .privateRepository(gameDTO.isPrivateRepository())
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
                .url(game.getUrl())
                .nameSpace(game.getNameSpace())
                .privateRepository(game.isPrivateRepository())
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

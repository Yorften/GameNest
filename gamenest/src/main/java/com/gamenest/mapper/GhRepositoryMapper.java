package com.gamenest.mapper;

import org.springframework.stereotype.Component;
import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.model.GhRepository;

@Component
public class GhRepositoryMapper {

    public GhRepository convertToEntity(GhRepositoryRequest ghRepositoryRequest) {
        if (ghRepositoryRequest == null) {
            return null;
        }
        return GhRepository.builder()
                .id(ghRepositoryRequest.getId())
                .ghId(ghRepositoryRequest.getGhId())
                .name(ghRepositoryRequest.getName())
                .fullName(ghRepositoryRequest.getFullName())
                .htmlUrl(ghRepositoryRequest.getHtmlUrl())
                .language(ghRepositoryRequest.getLanguage())
                .privateRepository(ghRepositoryRequest.isPrivateRepository())
                .build();
    }

    public GhRepositoryRequest convertToDTO(GhRepository ghRepository) {
        if (ghRepository == null) {
            return null;
        }
        return GhRepositoryRequest.builder()
                .id(ghRepository.getId())
                .ghId(ghRepository.getGhId())
                .name(ghRepository.getName())
                .fullName(ghRepository.getFullName())
                .htmlUrl(ghRepository.getHtmlUrl())
                .language(ghRepository.getLanguage())
                .privateRepository(ghRepository.isPrivateRepository())
                .build();
    }
}

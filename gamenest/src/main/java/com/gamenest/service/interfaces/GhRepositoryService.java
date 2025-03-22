package com.gamenest.service.interfaces;

import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.model.GhRepository;

public interface GhRepositoryService {

    GhRepository createRepository(GhRepositoryRequest repoRequest);

    void deleteRepository(Long repoId);

    GhRepository getRepositoryById(Long repoId);

    GhRepository getRepositoryByGhId(Long repoId);
}

package com.gamenest.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.model.GhRepository;
import com.gamenest.repository.GhRepositoryRepository;
import com.gamenest.service.interfaces.GhRepositoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GhRepositoryServiceImpl implements GhRepositoryService {

    private final GhRepositoryRepository ghRepositoryRepository;

    @Override
    public GhRepository createRepository(GhRepositoryRequest repoRequest) {
        GhRepository ghRepo = GhRepository.builder()
                .ghId(repoRequest.getGhId())
                .name(repoRequest.getName())
                .fullName(repoRequest.getFullName())
                .htmlUrl(repoRequest.getHtmlUrl())
                .language(repoRequest.getLanguage())
                .build();

        return ghRepositoryRepository.save(ghRepo);
    }

    @Override
    public void deleteRepository(Long repoId) {
        GhRepository existing = ghRepositoryRepository.findById(repoId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found"));
        ghRepositoryRepository.delete(existing);
    }

    @Override
    public GhRepository getRepositoryById(Long repoId) {
        return ghRepositoryRepository.findById(repoId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found"));
    }
}

package com.gamenest.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamenest.dto.repo.GhRepositoryRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.GhRepositoryMapper;
import com.gamenest.model.GhRepository;
import com.gamenest.repository.GhRepositoryRepository;
import com.gamenest.service.interfaces.GhRepositoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GhRepositoryServiceImpl implements GhRepositoryService {

    private final GhRepositoryRepository ghRepositoryRepository;
    private final GhRepositoryMapper ghRepositoryMapper;

    @Override
    public GhRepository createRepository(GhRepositoryRequest repoRequest) {
        GhRepository ghRepo = ghRepositoryMapper.convertToEntity(repoRequest);

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

    @Override
    public GhRepository getRepositoryByGhId(Long repoId) {
        return ghRepositoryRepository.findByGhId(repoId)
                .orElseThrow(() -> new ResourceNotFoundException("Repository not found"));
    }
}

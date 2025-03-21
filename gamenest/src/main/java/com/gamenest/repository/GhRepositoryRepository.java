package com.gamenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamenest.model.GhRepository;

import java.util.Optional;

public interface GhRepositoryRepository extends JpaRepository<GhRepository, Long> {
    Optional<GhRepository> findByGhId(Long ghId);
}

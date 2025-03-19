package com.gamenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamenest.model.Game;

/**
 * Repository interface for Game entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByTitle(String title);

    Optional<Game> findByRepositoryName(String repositoryName);
    
}

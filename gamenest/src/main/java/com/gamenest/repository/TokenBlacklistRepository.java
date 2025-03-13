package com.gamenest.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamenest.model.TokenBlacklist;

/**
 * Repository interface for Token blacklist entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    boolean existsByToken(String token);

    long deleteByCreatedAtBefore(LocalDateTime date);

}

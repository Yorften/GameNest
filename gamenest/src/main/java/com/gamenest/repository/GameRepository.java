package com.gamenest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gamenest.model.Game;

/**
 * Repository interface for Game entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Optional<Game> findByTitle(String title);

  Optional<Game> findByNameSpace(String nameSpace);

  List<Game> findByOwnerId(Long ownerId);

  @Query("""
        SELECT DISTINCT g
        FROM games g
             LEFT JOIN g.tags t
        WHERE
          (:categoryId IS NULL OR g.category.id = :categoryId)
          AND
          ((:tagIds) IS NULL OR t.id IN :tagIds)
      """)
  List<Game> findFiltered(@Param("categoryId") Long categoryId,
      @Param("tagIds") List<Long> tagIds);

}

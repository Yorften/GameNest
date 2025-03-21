package com.gamenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamenest.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}

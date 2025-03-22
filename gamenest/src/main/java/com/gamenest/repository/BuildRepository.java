package com.gamenest.repository;

import com.gamenest.model.Build;
import com.gamenest.model.enums.BuildStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildRepository extends JpaRepository<Build, Long> {

    List<Build> findByGame_IdOrderByCreatedAtDesc(Long gameId);

    List<Build> findByGame_IdAndBuildStatusOrderByCreatedAtDesc(Long gameId, BuildStatus status);
    
}
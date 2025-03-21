package com.gamenest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the GitHub repository details stored in the local database.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "game_repositories")
public class GhRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long ghId;

    private String name;

    private String fullName;

    private String htmlUrl;

    private String language;
    
    private boolean privateRepository;

    @OneToOne(mappedBy = "repository")
    private Game game;
}

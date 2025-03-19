package com.gamenest.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGameRequest {

    private String title;

    private String description;

    private String version;

    private String path;

    private String repositoryName;

    private Boolean privateRepository;

}

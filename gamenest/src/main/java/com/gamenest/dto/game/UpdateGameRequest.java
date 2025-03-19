package com.gamenest.dto.game;

import jakarta.validation.constraints.Size;
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

    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    private String description;

    private String version;

    private String path;

    private String repositoryName;

    private Boolean privateRepository;

}

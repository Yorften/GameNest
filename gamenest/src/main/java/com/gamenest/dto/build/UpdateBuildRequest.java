package com.gamenest.dto.build;

import com.gamenest.model.enums.BuildStatus;

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
public class UpdateBuildRequest {

    private BuildStatus buildStatus;
    private String logs;
    private String path;

}

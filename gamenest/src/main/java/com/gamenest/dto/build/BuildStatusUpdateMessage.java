package com.gamenest.dto.build;

import com.gamenest.model.enums.BuildStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildStatusUpdateMessage {
    private BuildRequest build;
    private Long buildId;
    private Long gameId;
    private BuildStatus status;
}


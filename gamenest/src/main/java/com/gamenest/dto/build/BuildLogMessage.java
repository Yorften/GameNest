package com.gamenest.dto.build;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildLogMessage {
    private Long buildId;
    private Long gameId;
    private String line;
}
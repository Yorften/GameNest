package com.gamenest.mapper;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.model.Build;
import org.springframework.stereotype.Component;

@Component
public class BuildMapper {

    public Build convertToEntity(BuildRequest dto) {
        if (dto == null)
            return null;
        return Build.builder()
                .id(dto.getId())
                .buildStatus(dto.getBuildStatus())
                .logs(dto.getLogs())
                .path(dto.getPath())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public BuildRequest convertToDTO(Build build) {
        if (build == null)
            return null;
        return BuildRequest.builder()
                .id(build.getId())
                .buildStatus(build.getBuildStatus())
                .logs(build.getLogs())
                .path(build.getPath())
                .createdAt(build.getCreatedAt())
                .updatedAt(build.getUpdatedAt())
                .build();
    }
}

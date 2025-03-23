package com.gamenest.service.interfaces;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.build.UpdateBuildRequest;
import com.gamenest.model.Build;

import java.util.List;

public interface BuildService {

    BuildRequest getLatestSuccessfulBuildByGameId(Long gameId);

    List<BuildRequest> getBuildsByGameId(Long gameId);

    Build createBuild(Build build);

    BuildRequest updateBuild(Long buildId, UpdateBuildRequest updateBuildRequest);
    
}
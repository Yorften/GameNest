package com.gamenest.service.interfaces;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.build.UpdateBuildRequest;
import java.util.List;

public interface BuildService {

    BuildRequest getLatestSuccessfulBuildByGameId(Long gameId);

    List<BuildRequest> getBuildsByGameId(Long gameId);

    BuildRequest createBuild(BuildRequest buildRequest);

    BuildRequest updateBuild(Long buildId, UpdateBuildRequest updateBuildRequest);
    
}
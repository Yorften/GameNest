package com.gamenest.service.implementation;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.build.UpdateBuildRequest;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.mapper.BuildMapper;
import com.gamenest.model.Build;
import com.gamenest.model.enums.BuildStatus;
import com.gamenest.repository.BuildRepository;
import com.gamenest.service.interfaces.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildServiceImpl implements BuildService {

    private final BuildRepository buildRepository;
    private final BuildMapper buildMapper;

    @Override
    public BuildRequest getLatestSuccessfulBuildByGameId(Long gameId) {
        List<Build> successfulBuilds = buildRepository.findByGame_IdAndBuildStatusOrderByCreatedAtDesc(gameId,
                BuildStatus.SUCCESS);
        if (successfulBuilds.isEmpty()) {
            throw new ResourceNotFoundException("No successful builds found for game id " + gameId);
        }
        Build latestSuccess = successfulBuilds.get(0);
        return buildMapper.convertToDTO(latestSuccess);
    }

    @Override
    public List<BuildRequest> getBuildsByGameId(Long gameId) {
        List<Build> builds = buildRepository.findByGame_IdOrderByCreatedAtDesc(gameId);
        return builds.stream()
                .map(buildMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Build createBuild(Build build) {
        return buildRepository.save(build);
    }

    @Override
    public BuildRequest updateBuild(Long buildId, UpdateBuildRequest updateBuildRequest) {
        Build existing = buildRepository.findById(buildId)
                .orElseThrow(() -> new ResourceNotFoundException("Build not found"));
        if (updateBuildRequest.getBuildStatus() != null) {
            existing.setBuildStatus(updateBuildRequest.getBuildStatus());
        }
        if (updateBuildRequest.getLogs() != null && !updateBuildRequest.getLogs().isEmpty()) {
            existing.setLogs(updateBuildRequest.getLogs());
        }
        if (updateBuildRequest.getPath() != null && !updateBuildRequest.getPath().isEmpty()) {
            existing.setPath(updateBuildRequest.getPath());
        }

        existing = buildRepository.save(existing);
        return buildMapper.convertToDTO(existing);
    }
}

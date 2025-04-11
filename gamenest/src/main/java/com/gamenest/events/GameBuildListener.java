package com.gamenest.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gamenest.dto.build.BuildLogMessage;
import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.build.BuildStatusUpdateMessage;
import com.gamenest.dto.build.UpdateBuildRequest;
import com.gamenest.model.Build;
import com.gamenest.model.Game;
import com.gamenest.model.enums.BuildStatus;
import com.gamenest.service.interfaces.BuildService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameBuildListener {

    private final BuildService buildService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${github.build.export-path}")
    private String buildExportPath;

    // Helper method to send status updates via WebSocket
    private void sendStatusUpdate(Build build, BuildStatus status) {
        if (build != null && build.getId() != null && build.getGame() != null && build.getGame().getId() != null) {
            Long gameId = build.getGame().getId();
            Long buildId = build.getId();

            String destination = String.format("/topic/builds/%d/status", gameId);
            BuildStatusUpdateMessage message = new BuildStatusUpdateMessage(buildId, gameId, status);
            log.debug("Sending status update to {}: {}", destination, message);

            messagingTemplate.convertAndSend(destination, message);
        } else {
            log.warn("Could not send status update, build or game info missing.");
        }
    }

    private void sendStatusUpdate(BuildRequest build, BuildStatus status) {
        if (build != null && build.getId() != null && build.getGame() != null && build.getGame().getId() != null) {
            Long gameId = build.getGame().getId();
            Long buildId = build.getId();

            String destination = String.format("/topic/builds/%d/status", gameId);
            BuildStatusUpdateMessage message = new BuildStatusUpdateMessage(buildId, gameId, status);
            log.debug("Sending status update to {}: {}", destination, message);

            messagingTemplate.convertAndSend(destination, message);
        } else {
            log.warn("Could not send status update, build or game info missing.");
        }
    }

    // Helper method to send log lines via WebSocket
    private void sendLogLine(Build build, String line) {
        if (build != null && build.getId() != null && build.getGame() != null && build.getGame().getId() != null) {
            Long buildId = build.getId();
            Long gameId = build.getGame().getId();

            String destination = String.format("/topic/builds/%d/logs", buildId);
            BuildLogMessage message = new BuildLogMessage(buildId, gameId, line);

            messagingTemplate.convertAndSend(destination, message);
        } else {
            log.warn("Could not send log line, build or game info missing.");
        }
    }

    @Async
    @EventListener
    public void handleGameBuildEvent(GameBuildEvent event) {
        Build build = null;
        BuildRequest updatedBuild = null;
        StringBuilder logs = new StringBuilder();
        File cloneDir = null;
        File buildOutputFolder = null;
        Git git = null;
        Game game = event.getGame();

        // 1) Create the build instance
        build = buildService.createBuild(Build.builder()
                .buildStatus(BuildStatus.RUNNING)
                .game(game)
                .build());

        sendStatusUpdate(build, BuildStatus.RUNNING);

        // log.info("path: {}", System.getProperty("user.dir"));
        try {
            // 2) Prepare a directory for cloning
            cloneDir = Files.createTempDirectory("repo-").toFile();

            // 3) Clone the repository (using JGit)
            String repoUrl = game.getRepository().getHtmlUrl() + ".git";
            String cloneMsg = "Cloning repository from " + repoUrl;
            logs.append(cloneMsg).append("\n");
            sendLogLine(build, cloneMsg);

            git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(cloneDir)
                    .call();
            String cloneDirMsg = "Repository cloned to " + cloneDir.getAbsolutePath();
            logs.append(cloneDirMsg).append("\n");
            sendLogLine(build, cloneDirMsg);

            // 4) Run the Godot command
            String startMsg = "Starting Godot build export...";
            logs.append(startMsg).append("\n");
            sendLogLine(build, startMsg);

            // 5) Copy export_rpesets.cfg to the cloned repository
            File sourceExportPresets = new File("./export/export_presets.cfg");

            File targetExportPresets = new File(cloneDir, "export_presets.cfg");
            try {
                // Delete the target file if it exists
                if (targetExportPresets.exists()) {
                    FileUtils.forceDelete(targetExportPresets);
                }
                // Copy and override
                FileUtils.copyFile(sourceExportPresets, targetExportPresets);
                String copyMsg = "Export preset copied to " + targetExportPresets.getAbsolutePath();
                logs.append(copyMsg).append("\n");
                sendLogLine(build, copyMsg);
            } catch (IOException ioe) {
                String errorMsg = "Failed to copy export preset: " + ioe.getMessage();
                logs.append(errorMsg).append("\n");
                sendLogLine(build, errorMsg);
                throw ioe;
            }

            // 6) Create a folder named build-{unique id} export path in export_rpesets.cfg
            // is empty so we will do it manualy with command args
            String buildFolderPath = buildExportPath + "-" + build.getId();
            buildOutputFolder = new File(buildFolderPath);
            if (!buildOutputFolder.exists()) {
                if (buildOutputFolder.mkdirs()) {
                    String createdMsg = "Build output folder created at: " + buildOutputFolder.getAbsolutePath();
                    logs.append(createdMsg).append("\n");
                    sendLogLine(build, createdMsg);
                    log.info("Build output folder created at: {}", buildOutputFolder);
                } else {
                    String failedMsg = "Failed to create build output folder at: "
                            + buildOutputFolder.getAbsolutePath();
                    logs.append(failedMsg).append("\n");
                    sendLogLine(build, failedMsg);
                    log.error("Failed to create build output folder at: {}", buildOutputFolder);
                    throw new IOException("Failed to create build output folder.");
                }
            }

            // 7) Prepare export path and project path
            String exportOutputPath = new File(buildOutputFolder, "index.html").getAbsolutePath();
            String projectFilePath = new File(cloneDir, "project.godot").getAbsolutePath();
            String exportMsg = "Export output will be written to: " + exportOutputPath;
            logs.append(exportMsg).append("\n");
            sendLogLine(build, exportMsg);
            log.info("Export output will be written to: {}", exportOutputPath);

            // 8) Get godot executable from PATH env and execute it
            String godotExecutablePath = System.getenv("GODOT_PATH");
            log.info("Attempting to use Godot executable from GODOT_PATH: {}", godotExecutablePath);

            if (godotExecutablePath == null || godotExecutablePath.isEmpty()) {
                String errorMsg = "GODOT_PATH environment variable is not set or is empty. Please set it to the full path of the Godot executable.";
                logs.append(errorMsg).append("\n");
                sendLogLine(build, errorMsg);
                throw new IllegalStateException(errorMsg);
            } else {
                // Check if the specified executable file exists (optional but recommended)
                File godotExecutableFile = new File(godotExecutablePath);
                if (!godotExecutableFile.exists() || !godotExecutableFile.isFile()) {
                    String errorMsg = "Godot executable specified by GODOT_PATH not found or is not a file: "
                            + godotExecutablePath;
                    logs.append(errorMsg).append("\n");
                    sendLogLine(build, errorMsg);
                    throw new IllegalStateException(errorMsg);
                }
                if (!godotExecutableFile.canExecute()) {
                    // Note: canExecute() might be unreliable on some systems (esp. Windows).
                    // You might skip this check or just log a warning.
                    String warnMsg = "Warning: Godot executable specified by GODOT_PATH may not be executable: "
                            + godotExecutablePath;
                    logs.append(warnMsg).append("\n");
                    sendLogLine(build, warnMsg);
                    log.warn("Godot executable specified by GODOT_PATH may not have execute permissions: {}",
                            godotExecutablePath);
                }

                String usingMsg = "Using Godot executable: " + godotExecutablePath;
                logs.append(usingMsg).append("\n");
                sendLogLine(build, usingMsg);
                log.info("Using Godot executable: {}", godotExecutablePath);

                // Use the godotExecutablePath directly from the environment variable
                ProcessBuilder pb = new ProcessBuilder(
                        godotExecutablePath,
                        "--headless",
                        projectFilePath,
                        "--export-release",
                        "Web",
                        exportOutputPath);

                pb.redirectErrorStream(true); // merge stderr into stdout

                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logs.append(line).append("\n");
                        sendLogLine(build, line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    log.info("Godot export command completed successfully.");
                    String successMsg = "Godot export succeeded.";
                    logs.append(successMsg).append("\n");
                    sendLogLine(build, successMsg);
                    updatedBuild = buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                            .buildStatus(BuildStatus.SUCCESS)
                            .logs(logs.toString())
                            .build());

                    sendStatusUpdate(updatedBuild, BuildStatus.SUCCESS);
                } else {
                    log.error("Godot export command failed with exit code: {}", exitCode);
                    String failMsg = "Godot export failed with exit code " + exitCode;
                    logs.append(failMsg).append("\n");
                    sendLogLine(build, failMsg);
                    updatedBuild = buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                            .buildStatus(BuildStatus.FAIL)
                            .logs(logs.toString())
                            .build());

                    sendStatusUpdate(updatedBuild, BuildStatus.FAIL);
                }
            }

        } catch (Exception e) {
            String errorMsg = "Build process failed: " + e.getMessage();
            logs.append(errorMsg).append("\n");
            if (build != null && build.getId() != null) {
                sendLogLine(build, errorMsg);
                log.error("Error during build {}: {}", build.getId(), e.getMessage(), e);
                updatedBuild = buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                        .buildStatus(BuildStatus.FAIL)
                        .logs(logs.toString())
                        .build());
                sendStatusUpdate(updatedBuild, BuildStatus.FAIL);
            } else {
                log.error("Error during build process before build entity could be fully initialized or persisted: {}",
                        e.getMessage(), e);
            }

            if (buildOutputFolder != null && buildOutputFolder.exists()) {
                try {
                    FileUtils.deleteDirectory(buildOutputFolder);
                    log.info("Cleaned up build output folder: {}", buildOutputFolder.getAbsolutePath());
                } catch (IOException ioEx) {
                    log.warn("Could not delete buildOutputFolder: {}", ioEx.getMessage());
                }
            }

        } finally {
            // 9) Clean up the cloned repository directory
            git.close();
            if (cloneDir != null && cloneDir.exists()) {
                try {
                    FileUtils.deleteDirectory(cloneDir);
                    log.info("Cleaned up clone directory: {}", cloneDir.getAbsolutePath());

                } catch (IOException ioEx) {
                    log.warn("Could not delete clone directory: {}", ioEx.getMessage());
                }
            }
            if (build != null) { // Final confirmation log
                sendLogLine(build, "Build process finished.");
                log.info("Finished handling build event for build {}", build.getId());
            } else {
                log.info("Finished handling build event (build may not have been created).");
            }
        }
    }
}

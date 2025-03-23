package com.gamenest.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gamenest.dto.build.BuildRequest;
import com.gamenest.dto.build.UpdateBuildRequest;
import com.gamenest.dto.game.GameRequest;
import com.gamenest.mapper.GameMapper;
import com.gamenest.model.Build;
import com.gamenest.model.enums.BuildStatus;
import com.gamenest.service.interfaces.BuildService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameBuildListener {

    private final BuildService buildService;
    private final GameMapper gameMapper;

    @Async
    @EventListener
    public void handleGameBuildEvent(GameBuildEvent event) {
        GameRequest gameRequest = gameMapper.convertToDTO(event.getGame());

        // 1) Create the build instance
        Build build = buildService.createBuild(BuildRequest.builder()
                .buildStatus(BuildStatus.RUNNING)
                .game(gameRequest)
                .build());

        StringBuilder logs = new StringBuilder();
        File cloneDir = null;
        File buildOutputFolder = null;
        Git git = null;

        // log.info("path: {}", System.getProperty("user.dir"));
        // C:\Users\Yorften\Documents\github\2222\GameNest\gamenest
        try {
            // 2) Prepare a directory for cloning
            cloneDir = Files.createTempDirectory("repo-").toFile();

            // 3) Clone the repository (using JGit)
            String repoUrl = event.getGame().getRepository().getHtmlUrl() + ".git";
            logs.append("Cloning repository from ").append(repoUrl).append("\n");

            git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(cloneDir)
                    .call();
            logs.append("Repository cloned to ").append(cloneDir.getAbsolutePath()).append("\n");

            // 4) Run the Godot command
            logs.append("Starting Godot build export...\n");

            // 5) Copy export_rpesets.cfg to the cloned repository
            File sourceExportPresets = new File(
                    "C:\\Users\\Yorften\\Documents\\github\\2222\\GameNest\\gamenest\\export\\export_presets.cfg");
            File targetExportPresets = new File(cloneDir, "export_presets.cfg");
            try {
                // Delete the target file if it exists
                if (targetExportPresets.exists()) {
                    FileUtils.forceDelete(targetExportPresets);
                }
                // Copy and override
                FileUtils.copyFile(sourceExportPresets, targetExportPresets);
                logs.append("Export preset copied to ").append(targetExportPresets.getAbsolutePath()).append("\n");
            } catch (IOException ioe) {
                logs.append("Failed to copy export preset: ").append(ioe.getMessage()).append("\n");
            }

            // 6) Create a folder named build-{unique id} export path in export_rpesets.cfg
            // is empty so we will do it manualy with command args
            String buildFolderPath = System.getProperty("user.dir") + "\\storage\\builds\\build-" + build.getId();
            buildOutputFolder = new File(buildFolderPath);
            if (!buildOutputFolder.exists()) {
                if (buildOutputFolder.mkdirs()) {
                    logs.append("Build output folder created at: ").append(buildOutputFolder.getAbsolutePath())
                            .append("\n");
                    log.info("Build output folder created at: {}", buildOutputFolder);
                } else {
                    logs.append("Failed to create build output folder at: ").append(buildOutputFolder.getAbsolutePath())
                            .append("\n");
                    log.info("Failed to create build output folder at: {}", buildOutputFolder);

                }
            }

            // 7) Append \index.html"
            String exportOutputPath = new File(buildOutputFolder, "index.html").getAbsolutePath();
            logs.append("Export output will be written to: ").append(exportOutputPath).append("\n");
            log.info("Export output will be written to: {}", exportOutputPath);

            // 8) Get godot executable from PATH env and execute it
            String godotInstallDir = System.getenv("GODOT_PATH");
            log.info("GODOT Env path: {}", godotInstallDir);
            if (godotInstallDir == null || godotInstallDir.isEmpty()) {
                logs.append("GODOT_PATH environment variable is not set.\n");
                throw new IllegalStateException("GODOT_PATH is not set in the environment.");
            } else {
                String godotExecutable = godotInstallDir + "\\Godot_v4.2.2-stable_win64.exe";
                logs.append("Using Godot executable at: ").append(godotExecutable).append("\n");
                log.info("Using Godot executable at: {}", godotExecutable);

                ProcessBuilder pb = new ProcessBuilder(
                        godotExecutable,
                        "--headless",
                        cloneDir.getAbsolutePath() + "/project.godot",
                        "--export-release",
                        "Web",
                        exportOutputPath);

                pb.redirectErrorStream(true); // merge stderr into stdout

                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logs.append(line).append("\n");

                        // TODO remove this in production
                        log.info(line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    log.error("Build succeeded!");
                    logs.append("Godot export succeeded.\n");
                    buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                            .buildStatus(BuildStatus.SUCCESS)
                            .logs(logs.toString())
                            .build());
                } else {
                    log.error("Build failed with error code: {}", exitCode);
                    logs.append("Godot export failed with exit code ").append(exitCode).append("\n");
                    buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                            .buildStatus(BuildStatus.FAIL)
                            .logs(logs.toString())
                            .build());
                }
            }
        } catch (Exception e) {
            logs.append("Build process failed: ").append(e.getMessage()).append("\n");
            buildService.updateBuild(build.getId(), UpdateBuildRequest.builder()
                    .buildStatus(BuildStatus.FAIL)
                    .logs(logs.toString())
                    .build());
            log.error("Error during build: {}", e.getMessage(), e);
            if (buildOutputFolder != null && buildOutputFolder.exists()) {
                try {
                    FileUtils.deleteDirectory(buildOutputFolder);
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
                } catch (IOException ioEx) {
                    log.warn("Could not delete cloneDir: {}", ioEx.getMessage());
                }
            }
        }

    }
}

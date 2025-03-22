package com.gamenest.controller.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamenest.exception.ResourceNotFoundException;
import com.gamenest.service.interfaces.GhRepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WebhookController {

    private final GhRepositoryService ghRepositoryService;
    @Value("${github.webhook.secret}")
    private String secret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Hub-Signature-256") String signatureHeader,
            @RequestHeader("X-GitHub-Event") String eventType,
            @RequestBody byte[] payload) {

        String computedSignature = "sha256=" + computeHmac256(payload, secret);

        if (!MessageDigest.isEqual(computedSignature.getBytes(StandardCharsets.UTF_8),
                signatureHeader.getBytes(StandardCharsets.UTF_8))) {
            // Signature doesn't match; ignore the request
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        switch (eventType) {
            case "push":
                log.info("Handling push event...");
                String payloadJson = new String(payload, StandardCharsets.UTF_8);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode;
                try {
                    rootNode = objectMapper.readTree(payloadJson);
                } catch (Exception e) {
                    log.error("Failed to parse push payload: {}", e.getMessage());
                    return ResponseEntity.badRequest().body("Invalid JSON");
                }

                JsonNode repositoryNode = rootNode.get("repository");
                String masterBranch = "<unknown branch>";

                Long repositoryId = null;

                if (repositoryNode != null) {
                    masterBranch = repositoryNode.get("master_branch").asText();
                    repositoryId = repositoryNode.get("id").asLong();
                }

                try {
                    ghRepositoryService.getRepositoryByGhId(repositoryId);
                } catch (Exception e) {
                    new ResourceNotFoundException("Repository not found");
                    log.info("The repository is not associated with any game, skipping build...");
                    return ResponseEntity.badRequest().body("Skipping build");
                }

                JsonNode refNode = rootNode.get("ref");
                JsonNode headCommitNode = rootNode.get("head_commit");

                String ref = (refNode != null) ? refNode.asText() : "<no ref>";
                String headCommitMessage = "<unknown commit>";
                String headCommitId = "<unknown commit>";

                if (headCommitNode != null) {
                    headCommitMessage = headCommitNode.get("message").asText();
                    headCommitId = headCommitNode.get("id").asText();
                }

                if (headCommitMessage.contains("{skip-build}")) {
                    log.info("Found skip build argument in commit message, skipping build...");
                    return ResponseEntity.badRequest().body("Skipping build");
                }

                if (ref.endsWith(masterBranch)) {
                    log.info("Received Push event on master branch ref: {}", ref);
                } else {
                    log.info("Push event is not on master branch {}, skipping build...", masterBranch);
                    return ResponseEntity.badRequest().body("Skipping build");
                }

                log.info("Starting build for commit {}", headCommitId);
                break;
            default:
                log.info("Ignoring event: {}", eventType);
        }

        return ResponseEntity.ok("Webhook received");
    }

    private String computeHmac256(byte[] data, String key) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hashBytes = hmac.doFinal(data);
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA-256", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

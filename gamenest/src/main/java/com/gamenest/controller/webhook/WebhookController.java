package com.gamenest.controller.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class WebhookController {

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

        // String payloadJson = new String(payload, StandardCharsets.UTF_8);
        // log.info("Received webhook payload: {}", payloadJson);
        log.info(eventType);
        switch (eventType) {
            case "push":
                log.info("Handling push event...");
                break;
            case "installation":
                log.info("Handling installation event...");
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

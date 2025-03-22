package com.gamenest.config.github;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${github.webhook.private-key-path}")
    private String privateKeyPath;

    /**
     * Reads the private key from the DER-formatted file.
     *
     * @return the PrivateKey object.
     * @throws Exception if an error occurs during key loading.
     */
    public PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Generates a JWT token for the GitHub App.
     *
     * @param githubAppId the GitHub App ID.
     * @param ttlMillis   the token's time-to-live in milliseconds (max 10 minutes).
     * @return a compact, URL-safe JWT string.
     * @throws Exception if token generation fails.
     */
    public String createJWT(String githubAppId, long ttlMillis) throws Exception {
        SignatureAlgorithm alg = Jwts.SIG.RS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        PrivateKey signingKey = getPrivateKey();

        JwtBuilder builder = Jwts.builder()
                .issuedAt(now)
                .issuer(githubAppId)
                .signWith(signingKey, alg);

        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.expiration(exp);
        }

        return builder.compact();
    }
}

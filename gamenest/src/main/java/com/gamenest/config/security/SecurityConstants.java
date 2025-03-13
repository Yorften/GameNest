package com.gamenest.config.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Data
@Component
public class SecurityConstants {

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package com.gamenest.config.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.gamenest.config.security.SecurityConstants;
import com.gamenest.exception.InvalidDataException;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil {

    private SecurityConstants securityConstants;

    public JwtTokenUtil(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + securityConstants.getJwtExpiration());
        MacAlgorithm alg = Jwts.SIG.HS512;

        String token = Jwts.builder().subject(username).issuedAt(currentDate).expiration(expiryDate)
                .signWith(securityConstants.getSigningKey(), alg).compact();

        return token;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().verifyWith(securityConstants.getSigningKey()).build().parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(securityConstants.getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new InvalidDataException("Invalid JWT token");
        }
    }

}

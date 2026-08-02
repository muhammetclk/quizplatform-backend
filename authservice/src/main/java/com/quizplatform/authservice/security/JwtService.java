package com.quizplatform.authservice.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    public String generateAccessToken(String userId, String email, String role) {
        return buildToken(userId, email, role, expiration);
    }

    public String generateRefreshToken(String userId, String email) {
        return buildToken(userId, email, null, refreshExpiration);
    }

    private String buildToken(String userId, String subject, String role, long expirationTime) {
        var builder = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey());

        if (role != null) {
            builder.claim("role", role);
        }
        if (userId != null) {
            builder.claim("userId", userId);
        }

        return builder.compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            if (isTokenBlacklisted(token)) return false;
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void blacklistToken(String token) {
        long remaining = getRemainingExpiration(token);
        if (remaining > 0) {
            redisTemplate.opsForValue()
                .set("blacklist:" + token, "logout", remaining, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }

    private long getRemainingExpiration(String token) {
        Date expDate = parseClaims(token).getExpiration();
        return expDate.getTime() - System.currentTimeMillis();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
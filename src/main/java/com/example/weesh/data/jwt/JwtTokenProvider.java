package com.example.weesh.data.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.weesh.data.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;

    public static final String BEARER = "Bearer";
    private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_VALID_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    public JwtTokenProvider(UserDetailsService userDetailsService, RedisService redisService, @Value("${jwt.secret}") String secretKey) {
        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public JwtTokenResponse generateToken(String username, Authentication authentication, Long userId) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("userId", userId)
                .add("type", "access")
                .build();

        Claims refreshClaims = Jwts.claims()
                .subject(username)
                .add("userId", userId)
                .add("type", "refresh")
                .build();

        Date now = new Date();
        Date accessExpire = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);
        Date refreshExpire = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        String accessToken = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(accessExpire)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .claims(refreshClaims)
                .expiration(refreshExpire)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        redisService.setValues(username, refreshToken, Duration.ofMillis(REFRESH_TOKEN_VALID_TIME));

        return new JwtTokenResponse(accessToken, refreshToken, BEARER, ACCESS_TOKEN_VALID_TIME);
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
package com.example.weesh.data.jwt;

import com.example.weesh.core.auth.application.jwt.TokenService;
import com.example.weesh.core.auth.exception.AuthErrorCode;
import com.example.weesh.core.auth.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.weesh.data.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class TokenServiceImpl implements TokenService {
    private final SecretKey key;
    public static final String BEARER = "Bearer";
    public static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    public TokenServiceImpl(UserDetailsService userDetailsService, RedisService redisService, @Value("${jwt.secret}") String secretKey) {
        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public JwtTokenResponse generateToken(String username, Long userId) {
        Claims claims = Jwts.claims().subject(username).add("userId", userId).add("type", "access").build();
        Claims refreshClaims = Jwts.claims().subject(username).add("userId", userId).add("type", "refresh").build();

        Date now = new Date();
        Date accessExpire = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);
        Date refreshExpire = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        String accessToken = Jwts.builder().claims(claims).issuedAt(now).expiration(accessExpire).signWith(key, Jwts.SIG.HS256).compact();
        String refreshToken = Jwts.builder().claims(refreshClaims).expiration(refreshExpire).signWith(key, Jwts.SIG.HS256).compact();

        try {
            redisService.setValues(username, refreshToken, Duration.ofMillis(REFRESH_TOKEN_VALID_TIME));
        } catch (Exception e) {
            log.error("Failed to store refresh token in Redis for username: {}, error: {}", username, e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "리프레시 토큰 저장 실패");
        }
        return new JwtTokenResponse(accessToken, refreshToken, BEARER, ACCESS_TOKEN_VALID_TIME);
    }

    @Override
    public void storeNewRefreshToken(String username, String refreshToken, long validityMillis) {
        try {
            redisService.setValues(username, refreshToken, Duration.ofMillis(validityMillis));
        } catch (Exception e) {
            log.error("Failed to store new refresh token in Redis for username: {}, error: {}", username, e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "새로운 리프레시 토큰 저장 실패");
        }
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    @Override
    public String getStoredRefreshToken(String username) {
        return redisService.getValues(username);
    }

    @Override
    public void invalidateRefreshToken(String username) {
        redisService.deleteValues(username);
    }

    @Override
    public void blacklistAccessToken(String token) {
        Long expiration = getExpiration(token);
        long ttl = expiration - System.currentTimeMillis();
        if (ttl > 0) {
            redisService.setValues("blacklist:" + token, "logout", Duration.ofMillis(ttl));
        }
    }

    @Override
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER + " ")) {
            String token = bearerToken.substring(7); // "Bearer " 제거
            String tokenType = getTokenType(token);
            if ("refresh".equals(tokenType)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            log.error("Token is missing or empty");
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "토큰이 비어있거나 누락되었습니다.");
        }
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Date now = new Date();
            if (claims.getExpiration().before(now)) {
                log.warn("만료된 JWT 토큰: expiration = {}", claims.getExpiration());
                throw new AuthException(AuthErrorCode.EXPIRED_TOKEN, "토큰이 만료되었습니다.");
            }
            return true;
        } catch (SecurityException e) {
            log.error("잘못된 JWT 서명: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰 구조: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "잘못된 JWT 토큰 구조입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "유효하지 않은 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "토큰 검증 중 오류가 발생했습니다. 토큰을 다시 확인해주세요.");
        }
    }

    @Override
    public String getTokenType(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("type", String.class);
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith(TokenServiceImpl.BEARER + " ")) ? bearerToken.substring(7) : null;
    }
    private Long getExpiration(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().getTime();
    }
}
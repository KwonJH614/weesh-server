package com.example.weesh.core.auth.application.jwt;

import com.example.weesh.data.jwt.JwtTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    JwtTokenResponse generateToken(String username, Long userId);
    String getUsername(String token);
    String getStoredRefreshToken(String username);
    void invalidateRefreshToken(String username);
    void blacklistAccessToken(String accessToken);
    String resolveRefreshToken(HttpServletRequest request);
    boolean validateToken(String token);
    String resolveToken(HttpServletRequest request);
    String getTokenType(String token);
    void storeNewRefreshToken(String username, String refreshToken, long validityMillis);
}

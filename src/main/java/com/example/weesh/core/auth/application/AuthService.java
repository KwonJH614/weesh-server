package com.example.weesh.core.auth.application;

import com.example.weesh.core.auth.application.jwt.TokenService;
import com.example.weesh.core.auth.application.useCase.AuthUseCase;
import com.example.weesh.core.auth.exception.AuthErrorCode;
import com.example.weesh.core.auth.exception.AuthException;
import com.example.weesh.core.shared.PasswordValidator;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.data.jwt.JwtTokenResponse;
import com.example.weesh.data.jwt.TokenServiceImpl;
import com.example.weesh.web.auth.dto.AuthRequestDto;
import com.example.weesh.web.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private final AuthRepository authRepository;
    private final TokenService tokenService;
    private final PasswordValidator passwordValidator;

    @Override
    public JwtTokenResponse login(AuthRequestDto dto) {
        User user = authRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        passwordValidator.validate(dto.getPassword(), user.getPassword());
        return tokenService.generateToken(dto.getUsername(), user.getId());
    }

    @Override
    public Map<String, Object> getProfileWithPortfolios() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        Map<String, Object> response = new HashMap<>();
        response.put("user", new UserResponseDto(user));
        return response;
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        tokenService.validateToken(refreshToken);
        String username = tokenService.getUsername(refreshToken);
        String storedRefreshToken = tokenService.getStoredRefreshToken(username);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN, "리프레시 토큰이 유효하지 않습니다.");
        }
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        JwtTokenResponse newTokens = tokenService.generateToken(username, user.getId());
        tokenService.invalidateRefreshToken(username); // 이전 리프레시 토큰 무효화
        tokenService.storeNewRefreshToken(username, newTokens.refreshToken(), TokenServiceImpl.REFRESH_TOKEN_VALID_TIME);
        return newTokens.accessToken();
    }

    @Override
    public Map<String, String> logout(String accessToken) {
        tokenService.validateToken(accessToken);
        String username = tokenService.getUsername(accessToken);
        tokenService.invalidateRefreshToken(username); // 리프레시 토큰 무효화
        tokenService.blacklistAccessToken(accessToken); // 액세스 토큰 블랙리스트
        return Map.of("message", "로그아웃 성공");
    }
}

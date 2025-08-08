package com.example.weesh.web.auth;

import com.example.weesh.core.auth.application.jwt.TokenService;
import com.example.weesh.core.auth.application.useCase.AuthUseCase;
import com.example.weesh.core.auth.exception.AuthException;
import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.data.jwt.JwtTokenResponse;
import com.example.weesh.data.jwt.TokenServiceImpl;
import com.example.weesh.web.auth.dto.AuthRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCase authUseCase;
    private final TokenService tokenService;

    public AuthController(AuthUseCase authUseCase, TokenService tokenService) {
        this.authUseCase = authUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> loginWithValidation(@Valid @RequestBody AuthRequestDto requestLogin) {
        JwtTokenResponse response = authUseCase.login(requestLogin);
        return ResponseEntity
                .ok(ApiResponse
                        .success("로그인 성공", response));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfile(HttpServletRequest request) {
        String accessToken = tokenService.resolveToken(request);
        tokenService.validateToken(accessToken);
        Map<String, Object> response = authUseCase.getProfileWithPortfolios();
        return ResponseEntity
                .ok(ApiResponse
                        .success("프로필 조회 성공", response));

    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> reissue(HttpServletRequest request) {
        String refreshToken = tokenService.resolveRefreshToken(request);
        JwtTokenResponse response = new JwtTokenResponse(
                authUseCase.reissueAccessToken(refreshToken),
                tokenService.getStoredRefreshToken(tokenService.getUsername(refreshToken)),
                TokenServiceImpl.BEARER,
                TokenServiceImpl.ACCESS_TOKEN_VALID_TIME
        );
        return ResponseEntity
                .ok(ApiResponse
                        .success("토큰 재발급 성공", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Map<String, String>>> logout(HttpServletRequest request) {
        String accessToken = tokenService.resolveToken(request); // resolveToken 메서드 추가 필요
        Map<String, String> response = authUseCase.logout(accessToken);
        return ResponseEntity
                .ok(ApiResponse
                        .success(response.get("message"), null));
    }
}
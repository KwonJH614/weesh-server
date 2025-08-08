package com.example.weesh.core.auth.application.useCase;

import com.example.weesh.data.jwt.JwtTokenResponse;
import com.example.weesh.web.auth.dto.AuthRequestDto;

import java.util.Map;

public interface AuthUseCase {
    JwtTokenResponse login(AuthRequestDto dto);
    Map<String, Object> getProfileWithPortfolios();
    String reissueAccessToken(String refreshToken);
    Map<String, String>  logout(String accessToken);
}
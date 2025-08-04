package com.example.weesh.data.jwt;

public record JwtTokenResponse(String accessToken, String refreshToken, String tokenType, Long accessTokenExpireDate) {}
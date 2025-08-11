package com.example.weesh.security.config;

import com.example.weesh.core.auth.application.jwt.TokenResolver;
import com.example.weesh.core.auth.application.jwt.TokenStorage;
import com.example.weesh.core.auth.application.jwt.TokenValidator;
import com.example.weesh.core.foundation.log.LoggingUtil;
import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.shared.PasswordValidator;
import com.example.weesh.data.redis.RedisService;
import com.example.weesh.security.auth.AccessTokenValidationStrategy;
import com.example.weesh.security.auth.AuthenticationContextManager;
import com.example.weesh.security.auth.RefreshTokenValidationStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final PathValidator pathValidator;
    private final AccessTokenValidationStrategy accessTokenStrategy;
    private final RefreshTokenValidationStrategy refreshTokenStrategy;
    private final AuthenticationContextManager authContextManager;
    private final ResponseHandler responseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        try {
            if (pathValidator.isPublicPath(requestURI)) {
                chain.doFilter(request, response);
                return;
            }

            List.of(refreshTokenStrategy, accessTokenStrategy)
                    .forEach(strategy -> {
                        try {
                            strategy.validate(request, response);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    });

            chain.doFilter(request, response);
        } catch (Exception e) {
            LoggingUtil.error("Authentication error for URI: {}, message: {}", requestURI, e.getMessage());
            authContextManager.clearAuthentication();
            responseHandler.sendErrorResponse(response, e.getMessage());
            return;
        }
    }
}
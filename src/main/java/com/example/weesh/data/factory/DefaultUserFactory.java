package com.example.weesh.data.factory;

import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.user.application.factory.UserFactory;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.core.foundation.enums.UserRole;
import com.example.weesh.web.user.dto.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component
public class DefaultUserFactory implements UserFactory {
    @Override
    public User createUserFromDto(UserRequestDto requestDto, String encryptedPassword) {
        Objects.requireNonNull(requestDto, "Request DTO cannot be null");
        validateRequestDto(requestDto);
        return User.builder()
                .username(requestDto.getUsername())
                .password(encryptedPassword)
                .fullName(requestDto.getFullName())
                .studentNumber(requestDto.getStudentNumber())
                .roles(Collections.singleton(UserRole.USER))
                .build();
    }

    private ApiResponse<Object> validateRequestDto(UserRequestDto dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return ApiResponse.error("아이디는 null 또는 빈 값일 수 없습니다.");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            return ApiResponse.error("비밀번호는 최소 8자 이상이어야 합니다.");
        }
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            return ApiResponse.error("이름은 null 또는 빈 값일 수 없습니다.");
        }
        if (dto.getStudentNumber() == null || dto.getStudentNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("학번은 null 또는 빈 값일 수 없습니다.");
        }
        return null;
    }
}

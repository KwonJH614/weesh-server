package com.example.weesh.core.user.application.useCase;


import com.example.weesh.core.shared.identifier.UserId;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.web.dto.UserRequestDto;

// useCase 인터페이스, 사용자 등록을 위한 메서드 정의
public interface registerUser {
    User register(UserRequestDto userRequestDto);
}

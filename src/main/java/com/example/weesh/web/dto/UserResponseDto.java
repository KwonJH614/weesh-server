package com.example.weesh.web.dto;

import com.example.weesh.core.shared.identifier.UserId;
import com.example.weesh.core.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final UserId id;
    private final String username;
    private final String fullName;
    private final String studentNumber;

    public UserResponseDto(User user) {
        this.id = user.getId(); // 최소한의 getter 사용
        this.username = user.getUsername(); // 임시, 이후 비즈니스 메서드로 대체 권장
        this.fullName = user.getFullName();
        this.studentNumber = user.getStudentNumber();
    }
}
package com.example.weesh.web.user.dto;

import com.example.weesh.core.foundation.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserRequestDto {
    @NotBlank(message = "아이디는 비어 있을 수 없습니다.")
    @Size(min = 2, message = "아이디는 최소 2글자 이상이어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 비어 있을 수 없습니다.")
    private String fullName;

    @NotBlank(message = "학번은 비어 있을 수 없습니다.")
    @Size(min = 4, max = 4, message = "학번은 4자리 숫자여야 합니다.")
    private String studentNumber;

    private Set<UserRole> roles;

    // 기본 생성자 (Jackson 시리얼라이제이션용)
    public UserRequestDto() {
    }

}
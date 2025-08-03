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
    // getter, setter
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Student number cannot be blank")
    private String studentNumber;

    private Set<UserRole> roles;

    // 기본 생성자 (Jackson 시리얼라이제이션용)
    public UserRequestDto() {
    }

}
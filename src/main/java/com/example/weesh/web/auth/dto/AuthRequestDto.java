package com.example.weesh.web.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuthRequestDto {
    @NotNull(message = "아이디는 필수 입력 값입니다.")
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}

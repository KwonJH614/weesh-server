package com.example.weesh.web.user;

import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.user.application.UserService;
import com.example.weesh.core.user.application.useCase.RegisterUserUseCase;
import com.example.weesh.web.user.dto.UserRequestDto;
import com.example.weesh.web.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = registerUserUseCase.register(userRequestDto);
        return ResponseEntity
                .ok(ApiResponse
                        .success("회원가입 성공",response)
        );
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<Object>> registerAdmin(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = registerUserUseCase.registerAdmin(userRequestDto);
        return ResponseEntity
                .ok(ApiResponse
                        .success("관리자 등록 성공", response));
    }
}

package com.example.weesh.web;

import com.example.weesh.core.user.application.UserService;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.web.dto.UserRequestDto;
import com.example.weesh.web.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody @Valid UserRequestDto requestDto) {
        User user = userService.register(requestDto);
        return new UserResponseDto(user); // 단일 인수로 수정
    }
}

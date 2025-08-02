package com.example.weesh.core.user.application;

import com.example.weesh.core.shared.identifier.UserId;
import com.example.weesh.core.user.application.useCase.registerUser;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.web.dto.UserRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements registerUser {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null");
    }

    @Transactional
    @Override
    public User register(UserRequestDto requestDto) {
        Objects.requireNonNull(requestDto, "Request DTO cannot be null");
        User user = User.create(
                requestDto.getUsername(),
                requestDto.getPassword(),
                requestDto.getFullName(),
                requestDto.getStudentNumber()
        );
        return userRepository.save(user);
    }
}

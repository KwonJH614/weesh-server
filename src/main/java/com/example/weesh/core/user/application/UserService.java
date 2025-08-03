package com.example.weesh.core.user.application;

import com.example.weesh.core.user.application.factory.UserFactory;
import com.example.weesh.core.user.application.useCase.RegisterUserUseCase;
import com.example.weesh.core.user.domain.User;
import com.example.weesh.core.user.domain.exception.DuplicateUserException;
import com.example.weesh.web.user.dto.UserRequestDto;
import com.example.weesh.web.user.dto.UserResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    // 추상체 분리 할 예정
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserFactory userFactory, PasswordEncoder passwordEncoder) {
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository cannot be null");
        this.userFactory = Objects.requireNonNull(userFactory, "UserFactory cannot be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "PasswordEncoder cannot be null");
    }

    @Transactional
    @Override
    public UserResponseDto register(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        User user = userFactory.createUserFromDto(dto, encryptedPassword);

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUserException("username", dto.getUsername());
        }
        if (userRepository.existsByStudentNumber(dto.getStudentNumber())) {
            throw new DuplicateUserException("studentNumber", dto.getStudentNumber());
        }
        return toResponseDto(userRepository.save(user));
    }

    // 별도 메서드로 UserResponseDto 생성 (SRP 준수)
    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user);
    }
}

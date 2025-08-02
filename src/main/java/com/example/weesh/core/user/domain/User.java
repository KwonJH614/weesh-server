// User 도메인 모델, 최소한의 JPA 사용
package com.example.weesh.core.user.domain;

import com.example.weesh.core.shared.identifier.UserId;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity(name = "users")
public class User implements Serializable {
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UserId id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String studentNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    // 기본 생성자 (JPA 요구)
    protected User() {
    }

    // 생성자, 비즈니스 로직 포함
    public User(UserId id, String username, String password, String fullName, String studentNumber) {
        // Objects.requireNonNull를 쓸지, 직접 메서드를 만들어서 유효성 검사할지 고민중..
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.fullName = validateFullName(fullName);
        this.studentNumber = validateStudentNumber(studentNumber);
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public User(String username, String password, String fullName, String studentNumber) {
    }

    // 유효성 검사 메서드
    private UserId validateId(UserId id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        return id;
    }

    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) throw new IllegalArgumentException("Username cannot be null or empty");
        return username;
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password must be at least 8 characters");
        return password;
    }

    private String validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) throw new IllegalArgumentException("Full name cannot be null or empty");
        return fullName;
    }

    private String validateStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) throw new IllegalArgumentException("Student number cannot be null or empty");
        return studentNumber;
    }

    // 비즈니스 메서드
    public void updateLastModifiedDate() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    public static User create(String username, String password, String fullName, String studentNumber) {
        return new User(username, password, fullName, studentNumber);
    }

}
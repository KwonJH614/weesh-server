// User 도메인 모델, 최소한의 JPA 사용
package com.example.weesh.core.user.domain;

import com.example.weesh.core.foundation.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "student_number", nullable = false, unique = true)
    private int studentNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    // 기본 생성자 (JPA 요구)
    protected User() {
    }

    // 생성자
    @Builder
    public User(String username, String password, String fullName, int studentNumber,
                LocalDateTime createdDate, LocalDateTime lastModifiedDate, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.studentNumber = studentNumber;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.lastModifiedDate = lastModifiedDate != null ? lastModifiedDate : LocalDateTime.now();
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    // 비즈니스 메서드
//    public void updateLastModifiedDate() {
//        this.lastModifiedDate = LocalDateTime.now();
//    }
//
//    public void addRole(UserRole role) {
//        if (role != null) this.roles.add(role);
//    }
}
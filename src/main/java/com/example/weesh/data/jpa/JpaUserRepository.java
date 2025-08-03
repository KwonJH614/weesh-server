package com.example.weesh.data.jpa;

import com.example.weesh.core.user.application.UserRepository;
import com.example.weesh.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    // JpaRepository가 제공하는 기본 CRUD 메서드와 UserRepository에서 정의한 메서드를 함께 사용할 수 있습니다.
    // 추가적인 쿼리 메서드가 필요하다면 여기에서 정의할 수 있습니다.
    boolean existsByUsername(String username);
    boolean existsByStudentNumber(String studentNumber);
}

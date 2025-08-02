package com.example.weesh.data.jpa;

import com.example.weesh.core.shared.identifier.UserId;
import com.example.weesh.core.user.application.UserRepository;
import com.example.weesh.core.user.domain.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository {
    // JpaUserRepository는 UserRepository 인터페이스를 구현하여 JPA를 사용한 사용자 데이터 저장소를 제공합니다.
    // 이 클래스는 JPA의 EntityManager를 사용하여 사용자 데이터를 데이터베이스에 저장하고 조회하는 기능을 구현합니다.

    // 예시로, EntityManager를 주입받아 사용할 수 있습니다.
     private final EntityManager entityManager;

     public JpaUserRepository(EntityManager entityManager) {
         this.entityManager = entityManager;
     }

    @Override
    public User save(User user) {
        // JPA를 사용하여 사용자 정보를 저장하는 로직을 구현합니다.
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        entityManager.persist(user);
        return user; // 저장 후 동일 객체 반환
    }

    @Override
    public User findById(UserId id) {
        // JPA를 사용하여 사용자 ID로 사용자 정보를 조회하는 로직을 구현합니다.
        if (id == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return entityManager.find(User.class, id);
    }
}

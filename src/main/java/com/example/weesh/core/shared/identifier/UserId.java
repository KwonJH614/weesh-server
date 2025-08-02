// UserId 값 객체, 여러 서브도메인에서 재사용 가능
package com.example.weesh.core.shared.identifier;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class UserId implements Serializable {
    // getter
    private Long value;

    // 기본 생성자 (JPA 요구)
    protected UserId() {
    }

    // 생성자, 유효성 검사
    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Invalid User ID");
        }
        this.value = value;
    }

    // equals 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    // hashCode 오버라이드
    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
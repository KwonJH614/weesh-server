package com.example.weesh.core.foundation.enums;

import lombok.Getter;

@Getter
public enum AdviceStatus {
    PENDING("대기 중"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨"),
    DELETED("삭제됨"),
    COMPLETED("완료됨");

    private final String description;

    AdviceStatus(String description) {
        this.description = description;
    }
}

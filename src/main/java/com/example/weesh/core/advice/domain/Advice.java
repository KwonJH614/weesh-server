package com.example.weesh.core.advice.domain;

import com.example.weesh.core.foundation.enums.AdviceStatus;
import com.example.weesh.web.advice.dto.AdviceUpdateRequestDro;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Advice {
    private final Long id;
    private String desiredDate;
    private String desiredTime;
    private String content;
    private final Long userId;
    private final Integer studentNumber;
    private final String fullName;
    private AdviceStatus status;
    private final LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Builder
    public Advice(Long id, String desiredDate, String desiredTime, String content, Long userId, Integer studentNumber, String fullName, AdviceStatus status, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.desiredDate = desiredDate;
        this.desiredTime = desiredTime;
        this.content = content;
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.status = status;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public void approve() {
        if (this.status != AdviceStatus.PENDING) {
            throw new IllegalStateException("PENDING(보류)상태의 상담만 승인될 수 있습니다. 현재 상태: " + this.status);
        }
        this.status = AdviceStatus.ACCEPTED;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void update(AdviceUpdateRequestDro dto) {
        LocalDate currentDate = LocalDate.now(); // 2025-09-04
        LocalDate adviceDate = LocalDate.parse(this.desiredDate != null ? this.desiredDate : LocalDate.now().toString());

        if (adviceDate.equals(currentDate)) {
            throw new IllegalStateException("당일에는 수정할 수 없습니다.");
        }
        if (this.status != AdviceStatus.PENDING) {
            throw new IllegalStateException("PENDING(보류)상태의 상담만 업데이트 할 수 있습니다. 현재 상태: " + this.status);
        }

        this.desiredDate = dto.getDesiredDate() != null ? dto.getDesiredDate() : this.desiredDate;
        this.desiredTime = dto.getDesiredTime() != null ? dto.getDesiredTime() : this.desiredTime;
        this.status = dto.getStatus() != null ? dto.getStatus() : this.status;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void delete() {
        LocalDate currentDate = LocalDate.now(); // 2025-09-04
        LocalDate adviceDate = LocalDate.parse(this.desiredDate != null ? this.desiredDate : LocalDate.now().toString());

        if (adviceDate.equals(currentDate)) {
            throw new IllegalStateException("당일에는 삭제할 수 없습니다.");
        }
        if (this.status != AdviceStatus.PENDING) {
            throw new IllegalStateException("PENDING(보류)상태의 상담만 삭제될 수 있습니다. 현재 상태: " + this.status);
        }

        this.status = AdviceStatus.DELETED;
        this.lastModifiedDate = LocalDateTime.now();
    }
}
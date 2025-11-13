package com.example.weesh.core.advice.application.useCase;

import com.example.weesh.web.advice.dto.AdviceResponseDto;

public interface AdviceApproveUseCase {
    AdviceResponseDto approveAdvice(Long adviceId);
}

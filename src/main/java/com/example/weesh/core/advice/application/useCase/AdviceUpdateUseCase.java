package com.example.weesh.core.advice.application.useCase;

import com.example.weesh.web.advice.dto.AdviceResponseDto;
import com.example.weesh.web.advice.dto.AdviceUpdateRequestDro;

public interface AdviceUpdateUseCase {
    AdviceResponseDto updateAdvice(Long adviceId, AdviceUpdateRequestDro dro);
}

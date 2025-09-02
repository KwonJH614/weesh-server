package com.example.weesh.core.advice.application.useCase;

import com.example.weesh.web.advice.dto.AdviceResponseDto;

import java.util.List;

public interface AdviceReadUseCase {
    List<AdviceResponseDto> getAdvice();
}

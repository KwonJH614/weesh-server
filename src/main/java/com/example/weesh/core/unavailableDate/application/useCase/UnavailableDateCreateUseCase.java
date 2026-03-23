package com.example.weesh.core.unavailableDate.application.useCase;

import com.example.weesh.web.unavailableDate.dto.UnavailableDateCreateRequestDto;
import com.example.weesh.web.unavailableDate.dto.UnavailableDateResponseDto;

public interface UnavailableDateCreateUseCase {
    UnavailableDateResponseDto createUnavailableDate(UnavailableDateCreateRequestDto dto);
}

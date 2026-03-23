package com.example.weesh.core.unavailableDate.application.useCase;

import com.example.weesh.web.unavailableDate.dto.UnavailableDateResponseDto;

import java.util.List;

public interface UnavailableDateReadUseCase {
    List<UnavailableDateResponseDto> getUnavailableDates();
}

package com.example.weesh.web.unavailableTime;

import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.unavailableTime.application.useCase.UnavailableTimeReadUseCase;
import com.example.weesh.web.unavailableTime.dto.UnavailableTimeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/unavailable-times")
@Tag(name = "UnavailableTime API", description = "예약 불가 시간 통합 조회 API")
@RequiredArgsConstructor
public class UnavailableTimeController {
    private final UnavailableTimeReadUseCase readUseCase;

    @Operation(summary = "예약 불가 시간 통합 조회", description = "상담 불가 시간과 이미 예약된 시간을 월 단위로 통합 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<UnavailableTimeResponseDto>>> getUnavailableTimes(
            @RequestParam String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth);
        List<UnavailableTimeResponseDto> response = readUseCase.getUnavailableTimes(ym);
        return ResponseEntity.ok(ApiResponse.success("예약 불가 시간 조회 성공", response));
    }
}

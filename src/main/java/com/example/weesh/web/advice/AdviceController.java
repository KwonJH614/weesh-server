package com.example.weesh.web.advice;

import com.example.weesh.core.advice.application.AdviceService;
import com.example.weesh.core.advice.application.useCase.AdviceCreateUseCase;
import com.example.weesh.core.advice.application.useCase.AdviceReadUseCase;
import com.example.weesh.core.foundation.log.LoggingUtil;
import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.web.advice.dto.AdviceCreateRequestDto;
import com.example.weesh.web.advice.dto.AdviceResponseDto;
import com.example.weesh.web.auth.dto.AuthRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advice")
@Tag(name = "Advice API", description = "상담 관련 API")
@RequiredArgsConstructor
public class AdviceController {
    private final AdviceCreateUseCase adviceCreateUseCase;
    private final AdviceReadUseCase adviceReadUseCase;

    @Operation(summary = "상담 예약", description = "회원/비회원 상담 예약")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "4xx || 5xx",
                    description = "실패"
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담 예약 요청 DTO",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AdviceCreateRequestDto.class)
            )
    )
    @PostMapping
    public ResponseEntity<ApiResponse<AdviceResponseDto>> createAdvice(
            @Valid @RequestBody AdviceCreateRequestDto dto,
            HttpServletRequest request) {
        AdviceResponseDto response = adviceCreateUseCase.createAdvice(dto, request);

        LoggingUtil.info("New advice created for student number: {}", dto.getFullName());
        return ResponseEntity
                .ok(ApiResponse
                        .success("상담 예약 성공", response));
    }


    @Operation(summary = "상담 예약 전체 조회", description = "관리자 권한")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<ResponseEntity<ApiResponse<AdviceResponseDto>>> getAdvice() {
        List<AdviceResponseDto> adviceList = adviceReadUseCase.getAdvice();
        return adviceList.stream()
                .map(advice -> ResponseEntity
                        .ok(ApiResponse
                                .success("상담 내역 조회 성공", advice)))
                .toList();
    }
}
package com.example.weesh.web.advice.dto;

import com.example.weesh.core.foundation.enums.AdviceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceUpdateRequestDro {
    private String desiredDate;
    private String desiredTime;
    private AdviceStatus status;
}

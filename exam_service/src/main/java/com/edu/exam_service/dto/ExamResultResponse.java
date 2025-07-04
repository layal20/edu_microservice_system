package com.edu.exam_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamResultResponse {
    private int score;
    private boolean passed;
    private String message;
}

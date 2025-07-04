package com.edu.exam_service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExamSubmissionRequest {
    private Long studentId;
    private Long examId;
    private Map<Long, String> answers; // questionId -> answer

}

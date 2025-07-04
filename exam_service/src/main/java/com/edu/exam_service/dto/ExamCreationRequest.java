package com.edu.exam_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamCreationRequest {
    private Long trainerId;
    private Long courseId;
    private int passingScore;
    private List<QuestionsDTO> questions;
}
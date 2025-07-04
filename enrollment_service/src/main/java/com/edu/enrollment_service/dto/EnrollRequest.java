package com.edu.enrollment_service.dto;

import lombok.Data;

@Data
public class EnrollRequest {
    private Long studentId;
    private Long courseId;
    private CourseStatus status;
}

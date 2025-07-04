package com.edu.enrollment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long trainerId;
    private CourseStatus status;
}

package com.edu.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long courseId;
    private Long studentId;
    private String status;
}

package com.edu.payment_service.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long studentId;
    private Long courseId;
    private Double amount;
}

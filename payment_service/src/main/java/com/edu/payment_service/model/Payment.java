package com.edu.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long courseId;
    private Double amount;
    private LocalDateTime paymentDate;
}

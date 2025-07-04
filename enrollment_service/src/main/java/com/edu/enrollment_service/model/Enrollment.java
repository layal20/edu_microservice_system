package com.edu.enrollment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "enrollments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Long studentId;

    private Long courseId;

    private boolean paid;

    private boolean passedExam;

    private LocalDateTime enrollmentDate;

    private LocalDateTime courseEndDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

}

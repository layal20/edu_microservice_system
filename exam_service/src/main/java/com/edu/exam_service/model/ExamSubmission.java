package com.edu.exam_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "ExamSubmissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long courseId;

    private Long examId;

    private int score;

    private boolean passed;

    private LocalDateTime submittedAt;

}

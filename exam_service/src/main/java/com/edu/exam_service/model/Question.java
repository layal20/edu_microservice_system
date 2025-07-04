package com.edu.exam_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}

package com.edu.exam_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private Long trainerId;

    private int passingScore;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}

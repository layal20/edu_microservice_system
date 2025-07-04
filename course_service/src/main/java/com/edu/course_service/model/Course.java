package com.edu.course_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double price;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.PENDING;
    @Column(name = "trainer_id")
    private Long trainerId;

}

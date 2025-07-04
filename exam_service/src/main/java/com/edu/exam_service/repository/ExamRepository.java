package com.edu.exam_service.repository;


import com.edu.exam_service.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByCourseId(Long courseId);
}

package com.edu.exam_service.repository;

import com.edu.exam_service.model.ExamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, Long> {
    Optional<ExamSubmission> findByCourseIdAndExamId(Long studentId, Long courseId);
    Optional<ExamSubmission> findByStudentIdAndExamId(Long studentId, Long examId);
    List<ExamSubmission> findByStudentId(Long studentId);

    boolean existsByStudentIdAndExamId(Long studentId, Long examId);
}

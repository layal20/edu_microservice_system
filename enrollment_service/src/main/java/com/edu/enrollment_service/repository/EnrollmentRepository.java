package com.edu.enrollment_service.repository;

import com.edu.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long CourseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudentIdAndPaid(Long studentId, Boolean paid);
}

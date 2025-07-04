package com.edu.course_service.repository;

import com.edu.course_service.model.Course;
import com.edu.course_service.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTrainerId(Long trainerId);

    boolean existsByTitleAndTrainerId(String title, Long trainerId);

    List<Course> findByStatus(CourseStatus status);

}

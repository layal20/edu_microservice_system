package com.edu.exam_service.client;

import com.edu.exam_service.dto.EnrollmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@FeignClient(name = "enrollment-service")
public interface EnrollmentClient {

    @GetMapping("/api/enrollments/id/{id}")
    EnrollmentResponse getEnrollmentById(@PathVariable("id") Long id);
    @GetMapping("/api/enrollments/course/{courseId}")
    List<EnrollmentResponse> getEnrollmentByCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/enrollments/student/{studentId}/course/{courseId}")
    EnrollmentResponse getEnrollmentByStudentAndCourse(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId);

    @PutMapping("/api/enrollments/updateStatus/{enrollmentId}")
    void updateStatus(
            @PathVariable("enrollmentId") Long enrollmentId,
            @RequestHeader("userId") Long userId,
            @RequestBody Map<String, String> body
    );

}
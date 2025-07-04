package com.edu.exam_service.service;

import com.edu.exam_service.client.EnrollmentClient;
import com.edu.exam_service.dto.EnrollmentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeEnrollmentService {

    private final EnrollmentClient enrollmentClient;

    @Retry(name = "enrollmentService", fallbackMethod = "fallback")
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallback")
    public EnrollmentResponse getEnrollmentById(Long id) {
        log.info("Fetching enrollment by id: {}", id);
        return enrollmentClient.getEnrollmentById(id);
    }

    @Retry(name = "enrollmentService", fallbackMethod = "fallbackList")
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallbackList")
    public List<EnrollmentResponse> getEnrollmentByCourseId(Long courseId) {
        log.info("Fetching enrollments for course: {}", courseId);
        return enrollmentClient.getEnrollmentByCourseId(courseId);
    }

    @Retry(name = "enrollmentService", fallbackMethod = "fallbackByStudentAndCourse")
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallbackByStudentAndCourse")
    public EnrollmentResponse getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        log.info("Fetching enrollment by student {} and course {}", studentId, courseId);
        return enrollmentClient.getEnrollmentByStudentAndCourse(studentId, courseId);
    }

    @Retry(name = "enrollmentService", fallbackMethod = "fallbackUpdate")
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallbackUpdate")
    public void updateStatus(Long enrollmentId, Long userId, Map<String, String> body) {
        log.info("Updating enrollment status: {}", enrollmentId);
        enrollmentClient.updateStatus(enrollmentId, userId, body);
    }

    public EnrollmentResponse fallback(Long id, Throwable t) {
        log.error("Fallback for getEnrollmentById. Reason: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Enrollment not found");
    }

    public List<EnrollmentResponse> fallbackList(Long courseId, Throwable t) {
        log.error("Fallback for getEnrollmentByCourseId. Reason: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollments not found for course ID : " + courseId);
    }

    public EnrollmentResponse fallbackByStudentAndCourse(Long studentId, Long courseId, Throwable t) {
        log.error("Fallback for getEnrollmentByStudentAndCourse. Reason: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Enrollments not found for student ID : " + studentId + "and course ID : " + courseId);
    }

    public void fallbackUpdate(Long enrollmentId, Long userId, Map<String, String> body, Throwable t) {
        log.error("Fallback for updateStatus. Reason: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE , "Enrollment service is currently unavailable");
    }

}

package com.edu.enrollment_service.service;


import com.edu.enrollment_service.client.CourseClient;
import com.edu.enrollment_service.dto.CourseResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeCourseService {

    private final CourseClient courseClient;

    @Retry(name = "courseService" , fallbackMethod = "getCourseByIdFallback")
    @CircuitBreaker(name = "CourseService", fallbackMethod = "getCourseByIdFallback")
    public CourseResponse getCourseById(Long id) {
        log.info("Trying to fetch course with ID: {}", id);

        return courseClient.getCourseById(id);
    }


    public CourseResponse getCourseByIdFallback(Long id, Throwable t) {
    log.error("Fallback triggered for course id: {}. Reason: {}", id, t.toString());

    if (t instanceof FeignException.NotFound) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
    }

    if (t instanceof FeignException.ServiceUnavailable || t.getMessage().contains("Connection refused")) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Course service is unavailable");
    }

    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + t.getMessage());
}
}

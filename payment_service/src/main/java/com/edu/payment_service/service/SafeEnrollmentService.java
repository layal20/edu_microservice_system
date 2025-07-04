package com.edu.payment_service.service;

import com.edu.payment_service.client.EnrollmentClient;
import com.edu.payment_service.dto.EnrollRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeEnrollmentService {

    private final EnrollmentClient enrollmentClient;

    @Retry(name = "enrollmentService", fallbackMethod = "fallback")
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallback")
    public ResponseEntity<?> enrollAfterPayment(EnrollRequest request) {
        log.info("Sending enroll request after payment...");
        return enrollmentClient.enrollAfterPayment(request);
    }

    public ResponseEntity<?> fallback(EnrollRequest request, Throwable t) {
        log.error("Fallback triggered in enrollAfterPayment. Reason: {}", t.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"Enrollment not found after payment");
    }

}

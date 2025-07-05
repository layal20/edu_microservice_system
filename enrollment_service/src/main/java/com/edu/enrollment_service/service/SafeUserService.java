package com.edu.enrollment_service.service;

import com.edu.enrollment_service.client.UserClient;
import com.edu.enrollment_service.dto.UserResponse;
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
public class SafeUserService {

    private final UserClient userClient;

    @Retry(name = "userService", fallbackMethod = "getUserByIdFallback")
    @CircuitBreaker(name = "userService", fallbackMethod = "getUserByIdFallback")
    public UserResponse getUserById(Long id) {
        log.info("Trying to fetch user with ID: {}", id);

        return userClient.getUserById(id);
    }


    public UserResponse getUserByIdFallback(Long id, Throwable t) {
        log.error("Fallback triggered for user id: {}. Reason: {}", id, t.toString());

        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (t instanceof FeignException.ServiceUnavailable || t.getMessage().contains("Connection refused")) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "User service is unavailable");
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + t.getMessage());
    }
}
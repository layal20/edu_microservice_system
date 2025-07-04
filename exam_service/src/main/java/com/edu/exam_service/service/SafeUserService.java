package com.edu.exam_service.service;

import com.edu.exam_service.client.UserClient;
import com.edu.exam_service.dto.UserResponse;
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
        log.error("Fallback triggered for user id: {}. Reason: {}", id, t.getMessage());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"User not found");
    }
}
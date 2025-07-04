package com.edu.exam_service.client;

import com.edu.exam_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/id/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

}
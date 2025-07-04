package com.edu.payment_service.client;

import com.edu.payment_service.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/api/courses/id/{id}")
    CourseResponse getCourseById(@PathVariable("id") Long id);
}
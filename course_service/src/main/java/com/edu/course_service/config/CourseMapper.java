package com.edu.course_service.config;

import com.edu.course_service.dto.CourseRequest;
import com.edu.course_service.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseRequest toDTO(Course course) {
        return CourseRequest.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .trainerId(course.getTrainerId())
                .status(course.getStatus())
                .build();
    }
}

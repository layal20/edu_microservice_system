package com.edu.course_service.dto;

import com.edu.course_service.model.CourseStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long trainerId;
    private CourseStatus status;

}

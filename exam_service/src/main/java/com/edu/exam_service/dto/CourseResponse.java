package com.edu.exam_service.dto;

import lombok.Data;

@Data
public class CourseResponse {
    private String id;
    private String title;
    private String description;
    private Long trainerId;
    private String price;
    private String status;

}

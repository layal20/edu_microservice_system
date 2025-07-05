package com.edu.course_service.controller;

import com.edu.course_service.client.UserClient;
import com.edu.course_service.config.CourseMapper;
import com.edu.course_service.dto.CourseRequest;
import com.edu.course_service.dto.CourseResponse;
import com.edu.course_service.model.Course;
import com.edu.course_service.repository.CourseRepository;
import com.edu.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserClient userClient;
    private final Environment environment;

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        try {
            CourseResponse response = courseService.addCourse(request);
            return ResponseEntity.ok(response);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<?> getApprovedCourses() {
        try{
            return ResponseEntity.ok(courseService.getAllCourses());
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingCourses(@RequestHeader("userId") Long userId) {
        try{
            return ResponseEntity.ok(courseService.getAllPendingCourses(userId));
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }

    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            CourseRequest user = courseService.findById(id);
            log.info("🎯 Handling course request on instance running at port {}", environment.getProperty("server.port"));

            return ResponseEntity.ok(user);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<?> getCoursesByTrainer(@PathVariable Long trainerId) {

        try {
            List<CourseResponse> courses = courseService.getCoursesByTrainer(trainerId);
            return ResponseEntity.ok(courses);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }

    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveCourse(@PathVariable Long id,
                                           @RequestHeader("userId") Long adminId) {
        try {
            return ResponseEntity.ok(courseService.approveCourse(id, adminId));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectCourse(@PathVariable Long id,
                                           @RequestHeader("userId") Long adminId) {

        try {
            return ResponseEntity.ok(courseService.rejectCourse(id,adminId));
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody Course courseData) {

        Long trainerId = courseData.getTrainerId();
        try {
            CourseResponse updated = courseService.updateCourse(id, courseData, trainerId);
            return ResponseEntity.ok(updated);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, @RequestHeader("userId") Long trainerId) {
        try {
            courseService.deleteCourse(id, trainerId);
            return ResponseEntity.ok("Course deleted successfully.");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
}


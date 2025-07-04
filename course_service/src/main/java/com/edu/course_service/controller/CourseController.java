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
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getApprovedCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingCourses(@RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(courseService.getAllPendingCourses(userId));
    }



    @GetMapping("/id/{id}")
    public ResponseEntity<CourseRequest> getById(@PathVariable long id) {
        CourseRequest user = courseService.findById(id);
        log.info("🎯 Handling course request on instance running at port {}", environment.getProperty("server.port"));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByTrainer(@PathVariable Long trainerId) {
        return ResponseEntity.ok(courseService.getCoursesByTrainer(trainerId));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveCourse(@PathVariable Long id,
                                           @RequestHeader("userId") Long adminId) {
        return ResponseEntity.ok(courseService.approveCourse(id, adminId));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectCourse(@PathVariable Long id,
                                           @RequestHeader("userId") Long adminId) {
        return ResponseEntity.ok(courseService.rejectCourse(id,adminId));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody Course courseData) {

        Long trainerId = courseData.getTrainerId();
        try {
            CourseResponse updated = courseService.updateCourse(id, courseData, trainerId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, @RequestHeader("userId") Long trainerId) {
        try {
            courseService.deleteCourse(id, trainerId);
            return ResponseEntity.ok("Course deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}

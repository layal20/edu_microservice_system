package com.edu.enrollment_service.controller;
import  com.edu.enrollment_service.dto.EnrollRequest;
import com.edu.enrollment_service.model.Enrollment;
import com.edu.enrollment_service.model.EnrollmentStatus;
import com.edu.enrollment_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/create")
    public ResponseEntity<?> enroll(@RequestBody EnrollRequest request) {
        try {

            return ResponseEntity.badRequest().body("You must pay first to enroll in the course.");
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }



    @PostMapping("/create-after-payment")
    public ResponseEntity<?> enrollAfterPayment(@RequestBody EnrollRequest request) {
        try {
            return ResponseEntity.ok(
                    enrollmentService.enrollAfterPayment(request.getStudentId(), request.getCourseId())
            );
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/student/{studentId}")
        public ResponseEntity<?> getStudentCourses(@PathVariable Long studentId) {

        try {

            return ResponseEntity.ok(enrollmentService.getEnrollmentsForStudent(studentId));
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollCourses(@PathVariable Long courseId){
        try {

            return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
      public ResponseEntity<?> getEnrollmentByStudentAndCourse(

            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {

            Enrollment enrollment = enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId);
            if (enrollment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(enrollment);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


    @PutMapping("/updateStatus/{enrollmentId}")
    public ResponseEntity<?> updateEnrollmentStatus(@PathVariable Long enrollmentId , @RequestHeader Long userId, @RequestBody Map<String, String> body) {

        try {
            EnrollmentStatus status = EnrollmentStatus.valueOf(body.get("status"));
            return ResponseEntity.ok(enrollmentService.updateStatus(enrollmentId, status, userId));
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }

    }




}
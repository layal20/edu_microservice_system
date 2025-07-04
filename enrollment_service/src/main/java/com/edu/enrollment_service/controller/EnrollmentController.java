package com.edu.enrollment_service.controller;
import  com.edu.enrollment_service.dto.EnrollRequest;
import com.edu.enrollment_service.model.Enrollment;
import com.edu.enrollment_service.model.EnrollmentStatus;
import com.edu.enrollment_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/create")
    public ResponseEntity<?> enroll(@RequestBody EnrollRequest request) {
        return ResponseEntity.badRequest().body("You must pay first to enroll in the course.");
    }



    @PostMapping("/create-after-payment")
    public ResponseEntity<?> enrollAfterPayment(@RequestBody EnrollRequest request) {
        try {
            return ResponseEntity.ok(
                    enrollmentService.enrollAfterPayment(request.getStudentId(), request.getCourseId())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentCourses(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsForStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    //public ResponseEntity<List<Enrollment>> getEnrollCourses(@PathVariable Long courseId) {
        public ResponseEntity<List<Enrollment>> getEnrollCourses(@PathVariable Long courseId){

        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        Enrollment enrollment = enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId);
        if (enrollment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enrollment);
    }


    @PutMapping("/updateStatus/{enrollmentId}")
    public ResponseEntity<?> updateEnrollmentStatus(@PathVariable Long enrollmentId , @RequestHeader Long userId, @RequestBody Map<String, String> body) {

        try {
            EnrollmentStatus status = EnrollmentStatus.valueOf(body.get("status"));
            return ResponseEntity.ok(enrollmentService.updateStatus(enrollmentId, status , userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
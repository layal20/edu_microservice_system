package com.edu.exam_service.controller;

import com.edu.exam_service.dto.ExamSubmissionRequest;
import com.edu.exam_service.model.ExamSubmission;
import com.edu.exam_service.service.ExamSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/exam-submissions")
@RequiredArgsConstructor
public class ExamSubmissionController {

    private final ExamSubmissionService submissionService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(@RequestBody ExamSubmissionRequest request) {
        try {
            ExamSubmission submission = submissionService.submitExam(request);
            return ResponseEntity.ok(submission);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


    @GetMapping("/student/{studentId}/exam/{examId}")
    public ResponseEntity<?> getSubmissionByStudentAndExam(
            @PathVariable Long studentId,
            @PathVariable Long examId) {
        try {
            ExamSubmission submission = submissionService.getSubmissionByStudentAndExam(studentId, examId);
            return ResponseEntity.ok(submission);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/course/{courseId}/exam/{examId}")
    public ResponseEntity<?> getSubmissionByCourseAndExam(
            @PathVariable Long courseId,
            @PathVariable Long examId) {
        try {
            ExamSubmission submission = submissionService.getSubmissionByCourseAndExam(courseId, examId);
            return ResponseEntity.ok(submission);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getSubmissionsForStudent(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(submissionService.getSubmissionsForStudent(studentId));
        }catch (
                ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


}

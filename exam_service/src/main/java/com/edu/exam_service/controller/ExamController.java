package com.edu.exam_service.controller;

import com.edu.exam_service.dto.*;
import com.edu.exam_service.model.Exam;
import com.edu.exam_service.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody ExamCreationRequest request) {
        try {
            Exam exam = examService.createExam(request);
            return ResponseEntity.ok(exam);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

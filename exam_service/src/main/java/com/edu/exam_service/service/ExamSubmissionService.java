package com.edu.exam_service.service;

import com.edu.exam_service.client.EnrollmentClient;
import com.edu.exam_service.client.UserClient;
import com.edu.exam_service.dto.EnrollmentResponse;
import com.edu.exam_service.dto.ExamSubmissionRequest;
import com.edu.exam_service.dto.UserResponse;
import com.edu.exam_service.model.Exam;
import com.edu.exam_service.model.ExamSubmission;
import com.edu.exam_service.model.Question;
import com.edu.exam_service.repository.ExamRepository;
import com.edu.exam_service.repository.ExamSubmissionRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamSubmissionService {

//    private final UserClient userClient;
//    private final EnrollmentClient enrollmentClient;

    private final SafeUserService safeUserService;
    private final SafeEnrollmentService safeEnrollmentService;

    private final ExamRepository examRepository;
    private final ExamSubmissionRepository submissionRepository;

    public ExamSubmission submitExam(ExamSubmissionRequest request) {
        UserResponse student;
            student = safeUserService.getUserById(request.getStudentId());

        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only students can submit exams");
        }

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Exam Not Found"));

        EnrollmentResponse enrollment;
            enrollment = safeEnrollmentService.getEnrollmentByStudentAndCourse(
                    request.getStudentId(), exam.getCourseId());


        if (!"PENDING_EXAM".equalsIgnoreCase(enrollment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not eligible to submit this exam");
        }

        if (submissionRepository.existsByStudentIdAndExamId(request.getStudentId(), request.getExamId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You have already submitted this exam");
        }

        int total = exam.getQuestions().size();
        int correct = 0;
        Map<Long, String> submittedAnswers = request.getAnswers();

        for (Question question : exam.getQuestions()) {
            String submittedAnswer = submittedAnswers.get(question.getId());
            if (question.getCorrectAnswer().equalsIgnoreCase(submittedAnswer)) {
                correct++;
            }
        }

        int score = (int) ((correct / (double) total) * 100);
        boolean passed = score >= 60;

        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", passed ? "PASSED" : "FAILED");

        safeEnrollmentService.updateStatus(
                enrollment.getId(),
                exam.getTrainerId(),
                statusUpdate
        );

        ExamSubmission submission = ExamSubmission.builder()
                .studentId(request.getStudentId())
                .examId(exam.getId())
                .courseId(exam.getCourseId())
                .score(score)
                .passed(passed)
                .submittedAt(LocalDateTime.now())
                .build();

        return submissionRepository.save(submission);
    }




    public ExamSubmission getSubmissionByStudentAndExam(Long studentId, Long examId) {
        return submissionRepository.findByStudentIdAndExamId(studentId, examId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Submission not found for studentId " + studentId + " and examId " + examId));
    }

    public ExamSubmission getSubmissionByCourseAndExam(Long courseId, Long examId) {
        return submissionRepository.findByCourseIdAndExamId(courseId, examId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Submission not found for courseId " + courseId + " and examId " + examId));
    }

    public List<ExamSubmission> getSubmissionsForStudent(Long studentId) {
        List<ExamSubmission> submissions = submissionRepository.findByStudentId(studentId);

        if (submissions.isEmpty()) {
            log.warn("No submissions found for studentId {}", studentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No submissions found for studentId " + studentId);
        }

        return submissions;
    }




}

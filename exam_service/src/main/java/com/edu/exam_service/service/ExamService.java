package com.edu.exam_service.service;

import com.edu.exam_service.client.*;
import com.edu.exam_service.dto.*;
import com.edu.exam_service.model.*;
import com.edu.exam_service.repository.*;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamSubmissionRepository submissionRepository;

    private final SafeUserService safeUserService;
    private final SafeCourseService safeCourseService;
    private final SafeEnrollmentService safeEnrollmentService;
    private final Environment environment;


    public Exam createExam(ExamCreationRequest request) {
            UserResponse trainer;
                trainer = safeUserService.getUserById(request.getTrainerId());


            if (!"TRAINER".equalsIgnoreCase(trainer.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only trainers can create exams.");
            }

            CourseResponse course;
                course = safeCourseService.getCourseById(request.getCourseId());


            if (!"APPROVED".equalsIgnoreCase(course.getStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only approved courses can have exams.");
            }

            if (!course.getTrainerId().equals(request.getTrainerId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Trainer is not assigned to this course.");
            }


                List<EnrollmentResponse> enrollments = safeEnrollmentService.getEnrollmentByCourseId(request.getCourseId());


                boolean hasPendingExam = enrollments.stream()
                        .anyMatch(enr -> "PENDING_EXAM".equalsIgnoreCase(enr.getStatus()));

                if (!hasPendingExam) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No students in this course are pending exam.");
                }

            Exam exam = Exam.builder()
                    .courseId(request.getCourseId())
                    .trainerId(request.getTrainerId())
                    .questions(new ArrayList<>())
                    .build();

            for (QuestionsDTO questionDTO : request.getQuestions()) {
                Question question = Question.builder()
                        .questionText(questionDTO.getQuestionText())
                        .correctAnswer(questionDTO.getCorrectAnswer())
                        .exam(exam)
                        .build();
                exam.getQuestions().add(question);
            }

            return examRepository.save(exam);




    }



}


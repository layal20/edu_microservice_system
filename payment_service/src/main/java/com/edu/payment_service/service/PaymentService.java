package com.edu.payment_service.service;

import com.edu.payment_service.dto.UserResponse;
import com.edu.payment_service.client.EnrollmentClient;
import com.edu.payment_service.dto.CourseResponse;
import com.edu.payment_service.dto.CourseStatus;
import com.edu.payment_service.dto.EnrollRequest;
import com.edu.payment_service.model.Payment;
import com.edu.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentClient enrollmentClient;

    private final SafeEnrollmentService safeEnrollmentService;
    private final SafeCourseService safeCourseService;
    private final SafeUserService safeUserService;
    private final Environment environment;


    public Payment payForCourse(Long studentId, Long courseId, Double amount) {
        CourseResponse course;
        UserResponse student;


            course = safeCourseService.getCourseById(courseId);

        student = safeUserService.getUserById(studentId);


        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only students can enroll");
        }

        if (!CourseStatus.APPROVED.equals(course.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only approved courses can be enrolled in");
        }

        if (!amount.equals(course.getPrice())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid payment amount. Expected: " + course.getPrice());
        }

        if (paymentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You have already paid for this course.");
        }

        Payment payment = Payment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .amount(amount)
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        EnrollRequest enrollRequest = new EnrollRequest();
        enrollRequest.setStudentId(studentId);
        enrollRequest.setCourseId(courseId);

        safeEnrollmentService.enrollAfterPayment(enrollRequest);

        return payment;
    }


}

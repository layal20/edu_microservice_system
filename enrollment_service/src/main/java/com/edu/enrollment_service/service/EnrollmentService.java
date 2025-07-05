package com.edu.enrollment_service.service;


import com.edu.enrollment_service.dto.CourseResponse;
import com.edu.enrollment_service.dto.CourseStatus;
import com.edu.enrollment_service.dto.UserResponse;
import com.edu.enrollment_service.model.Enrollment;
import com.edu.enrollment_service.model.EnrollmentStatus;
import com.edu.enrollment_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final SafeUserService safeUserService;
    private final SafeCourseService safeCourseService;
    private final Environment environment;


    public Enrollment enrollStudent(Long studentId, Long courseId) {
            UserResponse student = safeUserService.getUserById(studentId);
            CourseResponse course = safeCourseService.getCourseById(courseId);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endDate = now.plusDays(30);

            Enrollment enrollment = Enrollment.builder()
                    .studentId(studentId)
                    .courseId(courseId)
                    .enrollmentDate(now)
                    .courseEndDate(endDate)
                    .paid(false)
                    .status(EnrollmentStatus.ENROLLED)
                    .passedExam(false)
                    .build();

            return enrollmentRepository.save(enrollment);


    }





    public Enrollment enrollAfterPayment(Long studentId, Long courseId) {
        UserResponse student = safeUserService.getUserById(studentId);
        CourseResponse course = safeCourseService.getCourseById(courseId);
        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is not a student");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(30);

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .enrollmentDate(now)
                .courseEndDate(endDate)
                .paid(true)
                .passedExam(false)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        return enrollmentRepository.save(enrollment);
    }



    public List<Enrollment> getEnrollmentsForStudent(Long studentId) {
            UserResponse student = safeUserService.getUserById(studentId);

            if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is not a student");
            }

            List<Enrollment> list = enrollmentRepository.findByStudentId(studentId);
            if (list.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Student is not enrolled in any courses");
            }
            return list;

    }



    @Scheduled(cron = "0 0 0 * * ?")
    public void updateStatusesToPendingExam() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStatus() == EnrollmentStatus.ENROLLED &&
                    enrollment.getCourseEndDate().isBefore(now)) {
                enrollment.setStatus(EnrollmentStatus.PENDING_EXAM);
                enrollmentRepository.save(enrollment);
            }
        }
    }

    public Enrollment updateStatus(Long enrollmentId, EnrollmentStatus status , Long userId) {
            UserResponse user = safeUserService.getUserById(userId);

            if (!"TRAINER".equalsIgnoreCase(user.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only trainers can update status");
            }
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Enrollment not found"));
            CourseResponse course = safeCourseService.getCourseById(enrollment.getCourseId());

        if (!course.getTrainerId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not the trainer of this course.");
            }
//            if (enrollment.getStatus() != EnrollmentStatus.PENDING_EXAM) {
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only pending exams can be updated");
//            }
              if (enrollment.getStatus() == EnrollmentStatus.PASSED) {
                  throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Passed exm cannot be updated");
              }
            if (status == EnrollmentStatus.PASSED) {
                enrollment.setPassedExam(true);
            } else if (status == EnrollmentStatus.FAILED) {
                enrollment.setPassedExam(false);
            }
            enrollment.setStatus(status);
            return enrollmentRepository.save(enrollment);

    }
    public Enrollment getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Enrollment not found"));
    }



    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        CourseResponse course;
            course = safeCourseService.getCourseById(courseId);



        if (!CourseStatus.APPROVED.equals(course.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Course is not approved");
        }
        List<Enrollment> list = enrollmentRepository.findByCourseId(courseId);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;

    }
}






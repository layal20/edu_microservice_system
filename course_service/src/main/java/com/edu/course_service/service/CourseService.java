package com.edu.course_service.service;

import com.edu.course_service.config.CourseMapper;
import com.edu.course_service.dto.CourseRequest;
import com.edu.course_service.dto.CourseResponse;
import com.edu.course_service.dto.UserResponse;
import com.edu.course_service.model.Course;
import com.edu.course_service.model.CourseStatus;
import com.edu.course_service.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final SafeUserService safeUserService;
    private final Environment environment;


    public CourseResponse addCourse(CourseRequest request) {
        UserResponse trainer = safeUserService.getUserById(request.getTrainerId());

        if (!"TRAINER".equalsIgnoreCase(trainer.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only users with role TRAINER can add courses");
        }

        if (courseRepository.existsByTitleAndTrainerId(request.getTitle(), request.getTrainerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You already added this course");
        }

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTrainerId(request.getTrainerId());
        course.setPrice(request.getPrice());
        Course saved = courseRepository.save(course);
        return mapToResponse(saved);

    }


    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findByStatus(CourseStatus.APPROVED);
        if (courses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No courses found");
        }
        return courses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }



    public List<CourseResponse> getAllPendingCourses(long userId) {
        UserResponse user;
       // try {
            user = safeUserService.getUserById(userId);
//        } catch (FeignException e) {
//            String fullMessage = e.contentUTF8();
//            String cleanMessage = extractMessageFromJson(fullMessage);
//            throw new RuntimeException("Error while contacting User Service: " + cleanMessage);
//        }
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin not found");
//        }
        if (!user.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only admins can see pending courses");
        }

        List<Course> courses = courseRepository.findByStatus(CourseStatus.PENDING);
        if (courses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No pending courses found");
        }
        return courses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    public CourseRequest findById(long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        log.info("🎯 Handling course request on instance running at port {}", environment.getProperty("server.port"));

        return courseMapper.toDTO(course);
    }

    public List<CourseResponse> getCoursesByTrainer(Long trainerId) {
        List<Course> courses = courseRepository.findByTrainerId(trainerId);
        if (courses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No courses found for this trainer");
        }
        return courses.stream().map(this::mapToResponse).collect(Collectors.toList());
    }




    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .status(course.getStatus())
                .trainerId(course.getTrainerId())
                .build();
    }


    public CourseRequest approveCourse(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));

        UserResponse user ;
        user = safeUserService.getUserById(userId);

           if (!user.getRole().equals("ADMIN")) {
               throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only admins can approve courses");
           }
           if (course.getStatus().equals(CourseStatus.REJECTED)) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rejected courses cannot be approved.");
           }

        if (course.getStatus().equals(CourseStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This course is already approved.");
        }
           course.setStatus(CourseStatus.valueOf("APPROVED"));
           Course savedCourse = courseRepository.save(course);

           return courseMapper.toDTO(savedCourse);

    }




    public CourseRequest rejectCourse(Long courseId, Long userId) {


        UserResponse user;
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));

       // try {
            user = safeUserService.getUserById(userId);

//        } catch (FeignException e) {
//            if (e.status() == 404) {
//                throw new RuntimeException("Course or Student not found");
//            } else {
//                throw new RuntimeException("Remote call error: " + e.getMessage());
//            }
//        }
        if (!user.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only admins can reject courses");
        }
        if (course.getStatus().equals(CourseStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Approved courses cannot be rejected.");
        }
        if (course.getStatus().equals(CourseStatus.REJECTED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This course is already rejected.");
        }
        course.setStatus(CourseStatus.valueOf("REJECTED"));
        Course savedCourse = courseRepository.save(course);

        return courseMapper.toDTO(savedCourse);
    }



    public CourseResponse updateCourse(Long courseId, Course courseData, Long trainerId) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));

        if (!existingCourse.getTrainerId().equals(trainerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not allowed to edit this course.");
        }

        existingCourse.setTitle(courseData.getTitle());
        existingCourse.setDescription(courseData.getDescription());
        existingCourse.setPrice(courseData.getPrice());

        if (existingCourse.getStatus().equals(CourseStatus.APPROVED)) {
            existingCourse.setStatus(CourseStatus.PENDING);
        }

        Course updated = courseRepository.save(existingCourse);
        return mapToResponse(updated);
    }


    public void deleteCourse(Long courseId, Long trainerId) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Course not found"));

        if (!existingCourse.getTrainerId().equals(trainerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not allowed to delete this course.");
        }

        if (existingCourse.getStatus().equals(CourseStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Approved courses cannot be deleted.");
        }

        courseRepository.delete(existingCourse);
    }





}


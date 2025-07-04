package com.edu.payment_service.repository;

import com.edu.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}

package com.edu.payment_service.controller;

import com.edu.payment_service.dto.PaymentRequest;
import com.edu.payment_service.model.Payment;
import com.edu.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;





@PostMapping("/pay")
public ResponseEntity<?> pay(@RequestBody PaymentRequest request) {
    try {
        return ResponseEntity.ok(
                paymentService.payForCourse(request.getStudentId(), request.getCourseId(), request.getAmount())
        );
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}
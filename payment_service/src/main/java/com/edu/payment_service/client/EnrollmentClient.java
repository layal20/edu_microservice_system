package com.edu.payment_service.client;

import com.edu.payment_service.dto.EnrollRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "enrollment-service", path = "/api/enrollments")
public interface EnrollmentClient {

    @PostMapping("/create-after-payment")
    ResponseEntity<?> enrollAfterPayment(@RequestBody EnrollRequest request);
}

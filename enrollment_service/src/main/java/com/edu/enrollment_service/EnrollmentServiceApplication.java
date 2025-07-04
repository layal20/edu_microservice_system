package com.edu.enrollment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient

public class EnrollmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrollmentServiceApplication.class, args);
	}

}

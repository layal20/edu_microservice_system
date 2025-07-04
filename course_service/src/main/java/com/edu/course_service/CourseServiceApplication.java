package com.edu.course_service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor

@Slf4j

public class CourseServiceApplication {

	@Autowired
	private  Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}


	@PostConstruct
	public void init() {
		log.info("✅ course-service running on port {} registered with Eureka", environment.getProperty("server.port"));
	}
}

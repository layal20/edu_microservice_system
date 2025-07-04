package com.edu.exam_service.repository;

import com.edu.exam_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}

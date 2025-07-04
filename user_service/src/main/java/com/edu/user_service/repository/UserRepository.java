package com.edu.user_service.repository;

import com.edu.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findById(Long id);
}
package com.edu.user_service.config;

import com.edu.user_service.dto.UserDTO;
import com.edu.user_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
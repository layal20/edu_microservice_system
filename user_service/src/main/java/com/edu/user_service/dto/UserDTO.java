package com.edu.user_service.dto;

import com.edu.user_service.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
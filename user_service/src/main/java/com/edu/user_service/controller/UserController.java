package com.edu.user_service.controller;

import com.edu.user_service.dto.AuthRequest;
import com.edu.user_service.dto.UserDTO;
import com.edu.user_service.model.User;
import com.edu.user_service.service.UserService;
import com.edu.user_service.config.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return ResponseEntity.ok(userMapper.toDTO(savedUser));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers();

    }


    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            UserDTO user = userService.findById(id);
            return ResponseEntity.ok(user);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        try {
            UserDTO user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request){
        try {
            String response = userService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        }catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }






}

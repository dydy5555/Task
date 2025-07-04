package org.example.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.task.config.jwt.JwtUtil;
import org.example.task.model.request.LoginRequest;
import org.example.task.model.request.UserLogin;
import org.example.task.model.request.UserRegister;
import org.example.task.model.response.UserLoginResponse;
import org.example.task.model.user.Users;
import org.example.task.service.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "user registration 🤵")
    public ResponseEntity<UserLoginResponse> registerUser(@Valid @RequestBody UserRegister request) {
        UserLoginResponse newUser = userService.registerUser(request);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    @Operation(summary = "login with credentials 🗝️")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLogin request) {
        UserLoginResponse newUser = userService.loginUser(request);
        return ResponseEntity.ok(newUser);
    }

}

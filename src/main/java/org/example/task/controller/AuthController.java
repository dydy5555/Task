package org.example.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.task.config.jwt.JwtUtil;
import org.example.task.payload.user.ChangePassword;
import org.example.task.payload.user.UserLogin;
import org.example.task.payload.user.UserRegister;
import org.example.task.payload.user.UserLoginResponse;
import org.example.task.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "🔏")
@CrossOrigin
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

    @PutMapping("/change-password")
    @Operation(summary = "change password")
    public ResponseEntity<Boolean> changePassword (@Valid @RequestBody ChangePassword changePassword){
    return ResponseEntity.ok(userService.changePassword(changePassword));
    }
}

package org.example.task.payload.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email = "admin@gmail.com";
    private String password = "password";
//    private String provider;
}

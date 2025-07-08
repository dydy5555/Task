package org.example.task.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email = "admin@gmail.com";
    private String password = "password";
//    private String provider;
}

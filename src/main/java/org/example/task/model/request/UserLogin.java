package org.example.task.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with '+'")
    @Size(min = 1, max = 15, message = "Phone number must be between 1 and 15 characters")
    private String phoneNumber;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
package org.example.task.payload.user;

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
    @NotNull(message = "Cannot be null")
//    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with '+'")
    @Size(min = 1, max = 20, message = "Phone number must be between 1 and 20 characters")
    private String phoneOrGmail;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
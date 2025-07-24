package org.example.task.payload.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with '+'")
    private String phoneNumber;

    @NotBlank(message = "Current password is required")
    @Size(min = 6, message = "Current password must be at least 6 characters long")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "New password must be at least 6 characters long")
    private String newPassword;
}

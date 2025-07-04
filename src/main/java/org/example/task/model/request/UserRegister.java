package org.example.task.model.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.task.common.Provider;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    @NotNull(message = "Full name cannot be null")
    @Size(min = 1, max = 255, message = "Full name must be between 1 and 255 characters")
    private String fullName;

    @NotNull(message = "Gmail cannot be null")
    @Email(message = "Gmail should be a valid email address")
    private String gmail;

    @Size(min = 1, max = 15, message = "Phone number must be between 1 and 15 characters")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with '+'")
    private String phoneNumber;

//    @Size(max = 255, message = "Profile image URL must be less than 255 characters")
//    private String profileImage;

    private Provider provider;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String token;

//    @Size(min = 1, max = 6, message = "Code must be between 1 and 6 characters")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private String otpCode;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean status;

}

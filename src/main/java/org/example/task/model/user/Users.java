package org.example.task.model.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.task.common.Provider;
import org.example.task.common.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "users", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

//    @Size(max = 255, message = "Profile image URL must be less than 255 characters")
//    private String profileImage;

    @NotNull(message = "Full name cannot be null")
    @Size(min = 1, max = 255, message = "Full name must be between 1 and 255 characters")
    private String fullName;

    @NotNull(message = "Gmail cannot be null")
    @Email(message = "Gmail should be a valid email address")
    private String gmail;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 1, max = 15, message = "Phone number must be between 1 and 15 characters")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with '+'")
    private String phoneNumber;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Size(max = 255, message = "Chat ID must be less than 255 characters")
    private String chatId;

    private Boolean status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 20)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Provider provider;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status != null && this.status;
    }
}
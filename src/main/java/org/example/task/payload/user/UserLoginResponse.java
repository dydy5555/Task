package org.example.task.payload.user;import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.task.common.Provider;
import org.example.task.common.Role;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private UUID id;
//    private String profileImage;
    private String fullName;
    private String token;
    private String gmail;
    private String phoneNumber;
    private String chatId;
    private Boolean status;
//    private Boolean telegramVerify;
    private Provider provider;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;
    private ZonedDateTime loginTime;
    private Role role;
}

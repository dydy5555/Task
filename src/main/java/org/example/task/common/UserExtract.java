package org.example.task.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExtract {
    private UUID userId;
    private String gmail;
    private String phoneNumber;
    private String chatId;
    private String role;
    private String provider;
}
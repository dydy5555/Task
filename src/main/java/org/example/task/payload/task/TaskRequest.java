package org.example.task.payload.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.task.payload.user.UserTaskResponse;

import java.util.UUID;

@Data
public class TaskRequest {
    private UUID id;
    private String creatorId;
    private String title;
    private boolean completed = false;
    private UserTaskResponse user;
}

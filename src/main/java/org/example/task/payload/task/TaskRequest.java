package org.example.task.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskRequest {
    public String title;
    @Schema(description = "Is task completed", example = "false", defaultValue = "false")
    public boolean completed = false;
}

package org.example.task.model.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tasks", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    @NotNull(message = "Creator ID cannot be null")
    @Size(min = 1, max = 50, message = "Creator ID must be between 1 and 50 characters")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String creatorId;
    private String title;
    private boolean completed = false;
}

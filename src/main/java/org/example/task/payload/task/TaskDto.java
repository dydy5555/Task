package org.example.task.payload.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.task.model.task.Tasks;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Tasks task;
}

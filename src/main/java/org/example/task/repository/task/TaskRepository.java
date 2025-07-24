package org.example.task.repository.task;

import org.example.task.model.task.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Tasks, Long> {
}

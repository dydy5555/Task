package org.example.task.controller;

import org.example.task.model.task.Tasks;
import org.example.task.payload.task.TaskDto;
import org.example.task.payload.task.TaskRequest;
import org.example.task.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tasks> getAllTasks(){
        return service.getAllTask();
    }

    @GetMapping("/{id}")
    public List<Tasks> getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody Tasks task){
        TaskDto taskDto = service.createTask(task);
        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{id}")
    public Tasks updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest){
        return service.updateTask(id, taskRequest).orElseThrow(()-> new RuntimeException("Task not found!"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
          service.deleteTask(id);
    }

    @PatchMapping("/{id}/complete")
    public Tasks toggleCompleted(@PathVariable Long id){
        return service.toggleCompleted(id);
    }
}

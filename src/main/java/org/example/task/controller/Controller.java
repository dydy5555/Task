package org.example.task.controller;

import org.example.task.model.Task;
import org.example.task.model.request.TaskRequest;
import org.example.task.service.task.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class Controller {
    private final TaskService service;

    public Controller(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAllTasks(){
        return service.getAllTask();
    }

    @GetMapping("/{id}")
    public List<Task> getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public Task addTask(@RequestBody TaskRequest taskRequest){
        return service.addTask(taskRequest);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest){
        return service.updateTask(id, taskRequest).orElseThrow(()-> new RuntimeException("Task not found!"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
          service.deleteTask(id);
    }

    @PatchMapping("/{id}/complete")
    public Task toggleCompleted(@PathVariable Long id){
        return service.toggleCompleted(id);
    }
}

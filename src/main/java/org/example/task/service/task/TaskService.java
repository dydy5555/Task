package org.example.task.service.task;

import org.example.task.model.Task;
import org.example.task.model.request.TaskRequest;
import org.example.task.repository.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
    public List<Task> getAllTask(){
        return repository.findAll();
    }
    public List<Task> getById(Long id){
        return repository.findById(id).stream().toList();
    }
    public Task addTask(TaskRequest taskRequest){
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setCompleted(taskRequest.isCompleted());
        return repository.save(task);
    }
    public Optional<Task> updateTask(Long id, TaskRequest updateTask){
        return repository.findById(id).map(task ->{
            task.setTitle(updateTask.getTitle());
            task.setCompleted(updateTask.isCompleted());
            return repository.save(task);
        });
    }

    public void deleteTask (Long id){
        repository.deleteById(id);
    }

    public Task toggleCompleted(Long id){
        return repository.findById(id).map(task -> {
            task.setCompleted(!task.isCompleted());
            return repository.save(task);
        }).orElseThrow(()-> new RuntimeException("Task not found!"));
    }
}

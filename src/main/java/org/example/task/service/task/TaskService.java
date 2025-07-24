package org.example.task.service.task;

import jakarta.transaction.Transactional;
import org.example.task.config.jwt.JwtTokenFilter;
import org.example.task.config.jwt.JwtUtil;
import org.example.task.exception.NotFoundExceptionClass;
import org.example.task.model.task.Tasks;
import org.example.task.model.user.Users;
import org.example.task.payload.task.TaskDto;
import org.example.task.payload.task.TaskRequest;
import org.example.task.payload.user.UserTaskResponse;
import org.example.task.repository.task.TaskRepository;
import org.example.task.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final UserRepository userRepository;
    private  final JwtTokenFilter utils;
    public TaskService(TaskRepository repository, UserRepository userRepository, JwtTokenFilter utils) {
        this.repository = repository;
        this.userRepository = userRepository;

        this.utils = utils;
    }
    public List<Tasks> getAllTask(){
        return repository.findAll();
    }
    public List<Tasks> getById(Long id){
        return repository.findById(id).stream().toList();
    }

    public TaskRequest addTask(Tasks task){
        Optional<Users> users = userRepository.findById(UUID.fromString(task.getCreatorId()));
        if(users.isEmpty()){
            throw new NotFoundExceptionClass("User not found");
        }
        Users user = users.get();
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setId(task.getId());
        taskRequest.setCreatorId(task.getCreatorId());
        taskRequest.setTitle(task.getTitle());
        taskRequest.setCompleted(task.isCompleted());
        taskRequest.setUser(new UserTaskResponse(user.getFullName(),user.getGmail()));
        return taskRequest;
    }
    @Transactional
    public TaskDto createTask(Tasks tasks){
        tasks.setCreatorId(String.valueOf(utils.extractUser().getUserId()));
        tasks.setTitle(tasks.getTitle());
        tasks.setCompleted(false);
        Tasks saveTask = repository.save(tasks);
        return new TaskDto(saveTask);
    }
    public Optional<Tasks> updateTask(Long id, TaskRequest updateTask){
        return repository.findById(id).map(task ->{
            task.setTitle(updateTask.getTitle());
            task.setCompleted(updateTask.isCompleted());
            return repository.save(task);
        });
    }

    public void deleteTask (Long id){
        repository.deleteById(id);
    }

    public Tasks toggleCompleted(Long id){
        return repository.findById(id).map(task -> {
            task.setCompleted(!task.isCompleted());
            return repository.save(task);
        }).orElseThrow(()-> new RuntimeException("Task not found!"));
    }
}

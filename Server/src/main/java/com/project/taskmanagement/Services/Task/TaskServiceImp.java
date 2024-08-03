package com.project.taskmanagement.Services.Task;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.payload.Map.TaskMapper;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Sprint.SprintRepository;
import com.project.taskmanagement.repository.Task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public ResponseEntity<MessageResponse> addTask(TaskDto taskDto) {
        Optional<Sprint> sprint = sprintRepository.findById(taskDto.getSprintId());

        if (sprint.isPresent()) {
            Task task = taskMapper.toEntity(taskDto);
            task.setSprint(sprint.get());

            taskRepository.save(task);
            return ResponseEntity.ok(new MessageResponse("Task created successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sprint not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateTask(Long taskId, TaskDto taskDto) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task existingTask = task.get();
            taskMapper.updateEntityFromDto(taskDto, existingTask);
            if (taskDto.getSprintId() != null) {
                Optional<Sprint> sprint = sprintRepository.findById(taskDto.getSprintId());
                sprint.ifPresent(existingTask::setSprint);
            }

            taskRepository.saveAndFlush(existingTask);
            return ResponseEntity.ok(new MessageResponse("Task updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return ResponseEntity.ok(new MessageResponse("Task deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }
    }

    @Override
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDto> taskDtos = taskMapper.toDtoList(tasks);
        return ResponseEntity.ok(taskDtos);
    }

    @Override
    public ResponseEntity<TaskDto> getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.map(value -> ResponseEntity.ok(taskMapper.toDto(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @Override
    public ResponseEntity<List<TaskDto>> getTasksBySprint(Long sprintId) {
        Optional<Sprint> sprint = sprintRepository.findById(sprintId);
        if (sprint.isPresent()) {
            List<Task> tasks = taskRepository.findBySprint(sprint.get());
            List<TaskDto> taskDtos = taskMapper.toDtoList(tasks);
            return ResponseEntity.ok(taskDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}

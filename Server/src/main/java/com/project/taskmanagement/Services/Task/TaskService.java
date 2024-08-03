package com.project.taskmanagement.Services.Task;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.request.TaskRequest;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    ResponseEntity<MessageResponse> addTask(TaskDto taskDto);
    ResponseEntity<MessageResponse> updateTask(Long taskId, TaskDto taskDto);
    ResponseEntity<MessageResponse> deleteTask(Long taskId);
    ResponseEntity<List<TaskDto>> getAllTasks();
    ResponseEntity<TaskDto> getTaskById(Long taskId);
    ResponseEntity<List<TaskDto>> getTasksBySprint(Long sprintId);
}

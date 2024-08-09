package com.project.taskmanagement.Services.Task;

import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    ResponseEntity<MessageResponse> addTask(TaskDto taskDto);
    ResponseEntity<MessageResponse> updateTask(Long taskId, TaskDto taskDto);
    ResponseEntity<MessageResponse> deleteTask(Long taskId);
    ResponseEntity<List<TaskDto>> getAllTasks();
    ResponseEntity<TaskDto> getTaskById(Long taskId);
    ResponseEntity<List<TaskDto>> getTasksBySprint(Long sprintId);
    ResponseEntity<MessageResponse> assignUsersToTask(Long taskId, List<Long> userIds);
    ResponseEntity<MessageResponse> unassignUserFromTask(Long taskId, Long userId);
    ResponseEntity<MessageResponse> updateStatusAndOrder(Long taskId, TaskDto taskDto);
}

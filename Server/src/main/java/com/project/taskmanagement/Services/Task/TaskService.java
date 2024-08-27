package com.project.taskmanagement.Services.Task;

import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TaskService {
    ResponseEntity<MessageResponse> addTask(TaskDto taskDto);
    ResponseEntity<MessageResponse> updateTask(Long taskId, TaskDto taskDto);
    ResponseEntity<MessageResponse> deleteTask(Long taskId);
    ResponseEntity<List<TaskDto>> getAllTasks();
    ResponseEntity<TaskDto> getTaskById(Long taskId);
    ResponseEntity<List<TaskDto>> getTasksBySprint(Long sprintId);
    ResponseEntity<MessageResponse> assignUserToTask(Long taskId, Long userId);
    ResponseEntity<MessageResponse> unassignUserFromTask(Long taskId, Long userId);
    ResponseEntity<MessageResponse> updateStatusAndOrder(Long taskId, TaskDto taskDto);
    List<ProfilDto> getUsersByTaskId(Long taskId);
    List<TaskDto> getTasksByUserId();
    String getProjectNameOfTask(Long taskId);
    Map<String, Long> getTaskCountsByStatusForUser();
}

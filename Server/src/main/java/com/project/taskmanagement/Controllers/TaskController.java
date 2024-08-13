package com.project.taskmanagement.Controllers;

import com.project.taskmanagement.Services.Task.TaskService;
import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/addtask")
    public ResponseEntity<MessageResponse> addTask(@RequestBody TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

    @PutMapping("/updatetask/{id}")
    public ResponseEntity<MessageResponse> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/deltask/{id}")
    public ResponseEntity<MessageResponse> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("/alltask")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/tasksprint/{sprintId}")
    public ResponseEntity<List<TaskDto>> getTasksBySprint(@PathVariable Long sprintId) {
        return taskService.getTasksBySprint(sprintId);
    }
    @PostMapping("/userstasks/{taskId}/{userId}")
    public ResponseEntity<MessageResponse> assignUsersToTask(
            @PathVariable Long taskId,
            @PathVariable Long userId) {

        return taskService.assignUserToTask(taskId,userId);
    }
    @DeleteMapping("/unassignuser/{taskId}/{userId}")
    public ResponseEntity<MessageResponse> unassignUserFromTask(
            @PathVariable Long taskId,
            @PathVariable Long userId) {
        return taskService.unassignUserFromTask(taskId, userId);
    }
    @PutMapping("/updatestatus/{id}")
    public ResponseEntity<MessageResponse> updatestatus(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateStatusAndOrder(id, taskDto);
    }

    @GetMapping("/users/{taskId}")
    public List<ProfilDto> getUsersByTaskId(@PathVariable Long taskId) {
        return taskService.getUsersByTaskId(taskId);
    }
}

package com.project.taskmanagement.Services.Task;

import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Task;
import com.project.taskmanagement.Entities.User;
import com.project.taskmanagement.payload.Map.ProfilMapper;
import com.project.taskmanagement.payload.Map.SprintMapper;
import com.project.taskmanagement.payload.Map.TaskMapper;
import com.project.taskmanagement.payload.request.ProfilDto;
import com.project.taskmanagement.payload.request.SprintDto;
import com.project.taskmanagement.payload.request.TaskDto;
import com.project.taskmanagement.payload.response.MessageResponse;
import com.project.taskmanagement.repository.Auth.UserRepository;
import com.project.taskmanagement.repository.Sprint.SprintRepository;
import com.project.taskmanagement.repository.Task.TaskRepository;
import com.project.taskmanagement.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private SprintMapper sprintMapper;
    @Autowired
    private ProfilMapper profilMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<MessageResponse> addTask(TaskDto taskDto) {
        String validationMessage = validateTaskDates(taskDto);
        if (validationMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(validationMessage));
        }

        Optional<Sprint> sprint = sprintRepository.findById(taskDto.getSprintId());

        if (sprint.isPresent()) {
            Task task = taskMapper.toEntity(taskDto);
            task.setSprint(sprint.get());
            task.setStatus("To-Do");
            task.setTaskOrder(0);
            taskRepository.save(task);
            updateSprintStatus(task.getTaskId());
            return ResponseEntity.ok(new MessageResponse("Task created successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Sprint not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateTask(Long taskId, TaskDto taskDto) {
        LocalDate startDate = taskDto.getStartDate();
        LocalDate endDate = taskDto.getEndDate();
        if (endDate.isBefore(startDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("End date must be after start date."));
        }

        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task existingTask = task.get();
            taskMapper.updateEntityFromDto(taskDto, existingTask);
            if (taskDto.getSprintId() != null) {
                Optional<Sprint> sprint = sprintRepository.findById(taskDto.getSprintId());
                sprint.ifPresent(existingTask::setSprint);
            }

            taskRepository.saveAndFlush(existingTask);
            updateSprintStatus(taskId);
            return ResponseEntity.ok(new MessageResponse("Task updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }
    }

    private String validateTaskDates(TaskDto taskDto) {
        LocalDate startDate = taskDto.getStartDate();
        LocalDate endDate = taskDto.getEndDate();
        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            return "Start date and end date must be provided.";
        }

        if (endDate.isBefore(startDate)) {
            return "End date must be after start date.";
        }

        if (startDate.isBefore(today)) {
            return "Start date must be today or in the future.";
        }

        return null;
    }

    @Override
    public ResponseEntity<MessageResponse> deleteTask(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            updateSprintStatus(taskId);
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

    @Override
    public ResponseEntity<MessageResponse> assignUserToTask(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }

        Task task = taskOptional.get();
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User not found"));
        }

        Set<User> users = task.getUsers();

        if (users.contains(user)) {
            return ResponseEntity.ok(new MessageResponse("User already assigned!"));
        }

        users.add(user);
        task.setUsers(users);
        taskRepository.save(task);

        return ResponseEntity.ok(new MessageResponse("User assigned to task successfully!"));
    }

    @Override
    public ResponseEntity<MessageResponse> unassignUserFromTask(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Set<User> users = task.getUsers();

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (users.contains(user)) {
                    users.remove(user);
                    task.setUsers(users);
                    taskRepository.save(task);
                    return ResponseEntity.ok(new MessageResponse("User unassigned from task successfully!"));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new MessageResponse("User is not assigned to this task"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("User not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }
    }

    private void updateSprintStatus(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Optional<Sprint> sprintOptional = sprintRepository.findById(task.getSprint().getSprintId());
            if (sprintOptional.isPresent()) {
                Sprint sprint = sprintOptional.get();
                List<Task> tasks = taskRepository.findBySprint(sprint);

                List<TaskDto> taskDtos = tasks.stream()
                        .map(taskMapper::toDto)
                        .collect(Collectors.toList());

                String newStatus = determineSprintStatus(taskDtos);

                SprintDto sprintDto = sprintMapper.toDto(sprint);
                sprintDto.setStatus(newStatus);
                Sprint updatedSprint = sprintMapper.toEntity(sprintDto);
                sprintRepository.save(updatedSprint);
            }
        }
    }

    private String determineSprintStatus(List<TaskDto> taskDtos) {
        boolean hasDoing = taskDtos.stream().anyMatch(task -> "Doing".equals(task.getStatus()));
        boolean allToDo = taskDtos.stream().allMatch(task -> "To-Do".equals(task.getStatus()));
        boolean allDone = taskDtos.stream().allMatch(task -> "Done".equals(task.getStatus()));
        boolean hasToDo = taskDtos.stream().anyMatch(task -> "To-Do".equals(task.getStatus()));
        boolean hasDone = taskDtos.stream().anyMatch(task -> "Done".equals(task.getStatus()));

        if (hasDoing) {
            return "Doing";
        } else if (allToDo) {
            return "To-Do";
        } else if (allDone) {
            return "Done";
        } else if (hasToDo && hasDone) {
            return "To-Do";
        }
        return "To-Do";
    }
    @Override
    public ResponseEntity<MessageResponse> updateStatusAndOrder(Long taskId, TaskDto taskDto) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task existingTask = task.get();

            taskMapper.updateStatusAndOrderFromDto(taskDto, existingTask);

            if (taskDto.getSprintId() != null) {
                Optional<Sprint> sprint = sprintRepository.findById(taskDto.getSprintId());
                sprint.ifPresent(existingTask::setSprint);
            }

            taskRepository.saveAndFlush(existingTask);
            updateSprintStatus(taskId);
            return ResponseEntity.ok(new MessageResponse("Task updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Task not found"));
        }
    }
        @Override
        public List<ProfilDto> getUsersByTaskId(Long taskId) {
            // Récupérer la tâche en fonction de l'ID
            Optional<Task> taskOpt = taskRepository.findById(taskId);
            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();
                // Convertir les utilisateurs associés en ProfilDto
                return profilMapper.toDtoList(new ArrayList<>(task.getUsers()));
            } else {
                throw new RuntimeException("Task not found with ID: " + taskId);
            }
        }
        @Override
    public List<TaskDto> getTasksByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        List<Task> tasks = taskRepository.findByUsers_Id(userId);
        return taskMapper.toDtoList(tasks);
    }
    @Override
    public String getProjectNameOfTask(Long taskId) {
        return taskRepository.findProjectNameByTaskId(taskId);
    }
    @Override
    public Map<String, Long> getTaskCountsByStatusForUser() {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Exécuter la requête pour obtenir les comptes de tâches par statut
        List<Object[]> results = taskRepository.countTasksByStatusForUser(userId);

        // Log des résultats pour vérifier ce qui est retourné
        System.out.println("Résultats de la requête pour userId=" + userId + " : " + results);

        // Initialiser la map pour stocker les résultats
        Map<String, Long> taskCounts = new HashMap<>();

        // Parcourir les résultats et les insérer dans la map
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            taskCounts.put(status, count);
        }

        return taskCounts;
    }

}

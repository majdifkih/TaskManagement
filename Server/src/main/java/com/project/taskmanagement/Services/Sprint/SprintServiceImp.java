package com.project.taskmanagement.Services.Sprint;

import com.project.taskmanagement.Entities.Project;
import com.project.taskmanagement.Entities.Sprint;
import com.project.taskmanagement.Entities.Backlog;
import com.project.taskmanagement.payload.request.SprintRequest;
import com.project.taskmanagement.repository.Sprint.SprintRepository;
import com.project.taskmanagement.repository.Backlog.BacklogRepository;
import com.project.taskmanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SprintServiceImp implements SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Override
    public ResponseEntity<?> addSprint(SprintRequest sprintRequest) {
        try {
            Optional<Backlog> backlog = backlogRepository.findById(sprintRequest.getBacklog());
            LocalDateTime creationDate = LocalDateTime.now();
            if (backlog.isPresent()) {
                Sprint sprint = new Sprint();
                sprint.setSprintName(sprintRequest.getSprintName());
                sprint.setSprintDescription(sprintRequest.getSprintDescription());
                sprint.setStatus(sprintRequest.getStatus());
                sprint.setPriority(sprintRequest.getPriority());
                sprint.setCreationDate(creationDate);
                sprint.setEndDate(sprintRequest.getEndDate());
                sprint.setBacklog(backlog.get());

                sprintRepository.save(sprint);
                return ResponseEntity.ok(new MessageResponse("Sprint created successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Backlog not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while creating the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateSprint(Long sprintId, SprintRequest sprintRequest) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                Sprint existingSprint = sprint.get();
                existingSprint.setSprintName(sprintRequest.getSprintName());
                existingSprint.setSprintDescription(sprintRequest.getSprintDescription());
                existingSprint.setStatus(sprintRequest.getStatus());
                existingSprint.setPriority(sprintRequest.getPriority());
                existingSprint.setEndDate(sprintRequest.getEndDate());

                sprintRepository.saveAndFlush(existingSprint);
                return ResponseEntity.ok(new MessageResponse("Sprint updated successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while updating the Sprint: " + e.getMessage()));
        }
    }
    @Override
    public ResponseEntity<?> deleteSprint(Long sprintId) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                sprintRepository.delete(sprint.get());
                return ResponseEntity.ok(new MessageResponse("Sprint deleted successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while deleting the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllSprints() {
        try {
            List<Sprint> sprints = sprintRepository.findAll();
            return ResponseEntity.ok(sprints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the sprints: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getSprintById(Long sprintId) {
        try {
            Optional<Sprint> sprint = sprintRepository.findById(sprintId);
            if (sprint.isPresent()) {
                return ResponseEntity.ok(sprint.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Sprint not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the Sprint: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getSprintsByBacklog(Long backlogId) {
        try {
            Optional<Backlog> backlog = backlogRepository.findById(backlogId);
            if (backlog.isPresent()) {
                List<Sprint> sprints = sprintRepository.findByBacklog(backlog.get());
                return ResponseEntity.ok(sprints);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Backlog not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("An error occurred while retrieving the sprints: " + e.getMessage()));
        }
    }
}
